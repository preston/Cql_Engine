package org.harmoniq.cqlEngineService;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.*;

import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.elm.tracking.TrackBack;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.execution.Context;
import org.cqframework.cql.execution.CqlLibraryReader;

import org.cqframework.cql.data.fhir.FhirDataProvider;
import org.cqframework.cql.data.fhir.FhirMeasureEvaluator;

import org.hl7.fhir.dstu3.model.Measure;
import org.hl7.fhir.dstu3.model.Patient;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

@Path("evaluate")
public class EngineResource {

  Library library = null;
  private File xmlFile = null;

  @POST
  @Consumes({MediaType.TEXT_PLAIN})
  @Produces({MediaType.TEXT_PLAIN})
  public String evaluateCql(String cql) throws JAXBException, IOException {
    String[] names = getExpressionNames(cql);
    JSONArray resultArr = new JSONArray();
    try {
      translate(cql);
    } catch (IllegalArgumentException e) {
      JSONObject results = new JSONObject();
      results.put("translation-error", e.getMessage());
      resultArr.add(results);
      return resultArr.toJSONString();
    }

    Context context = new Context(library);
    // check for FHIR evaluation
    // if (cql.indexOf("using FHIR") != -1) {
    //   return evaluateFhir(cql, context);
    // }

    for (int i = 0; i < names.length; ++i) {
      JSONObject results = new JSONObject();
      try {
        results.put("name", names[i]);
        Object result = context.resolveExpressionRef(library, names[i]).getExpression().evaluate(context);
        // could consider including more information like type, operation being performed, etc...
        results.put("result", result == null ? "Null" : result.toString());
        String type = result == null ? "Null" : result.getClass().getSimpleName();
        if (type.equals("BigDecimal")) { type = "Decimal"; }
        else if (type.equals("ArrayList")) { type = "List"; }
        results.put("resultType", type);
      } catch (RuntimeException e) {
        results.put("error", e.getMessage());
      }
      resultArr.add(results);
    }
    return resultArr.toJSONString();
  }

  public String[] getExpressionNames(String cql) {
    List<String> allMatches = new ArrayList<String>();
    Matcher m = Pattern.compile("(?<=define\\s).*?(?=:)").matcher(cql);
    while (m.find()) { allMatches.add(m.group().replaceAll("\"", "").trim()); }
    return allMatches.toArray(new String[0]);
  }

  public void translate(String cql) throws JAXBException, IOException {
    LibraryManager libraryManager = new LibraryManager();
    try {
      ArrayList<CqlTranslator.Options> options = new ArrayList<>();
      options.add(CqlTranslator.Options.EnableDateRangeOptimization);
      CqlTranslator translator = CqlTranslator.fromText(cql, libraryManager, options.toArray(new CqlTranslator.Options[options.size()]));

      if (translator.getErrors().size() > 0) {
        System.err.println("Translation failed due to errors:");
        ArrayList<String> errors = new ArrayList<>();
        for (CqlTranslatorException error : translator.getErrors()) {
          TrackBack tb = error.getLocator();
          String lines = tb == null ? "[n/a]" : String.format("[%d:%d, %d:%d]",
                  tb.getStartLine(), tb.getStartChar(), tb.getEndLine(), tb.getEndChar());
          errors.add(lines + error.getMessage());
        }
        throw new IllegalArgumentException(errors.toString());
      }

      xmlFile = new File("response.xml");
      xmlFile.createNewFile();

      PrintWriter pw = new PrintWriter(xmlFile, "UTF-8");
      pw.println(translator.toXml());
      pw.println();
      pw.close();
    } catch (IOException e) {
        e.printStackTrace();
    }

    library = CqlLibraryReader.read(xmlFile);
  }

  // public String evaluateFhir(String cql, Context context) {
  //   FhirDataProvider provider = new FhirDataProvider().withEndpoint("http://fhirtest.uhn.ca/baseDstu3");
  //   //FhirDataProvider provider = new FhirDataProvider().withEndpoint("http://fhir3.healthintersections.com.au/open/");
  //   //FhirDataProvider provider = new FhirDataProvider().withEndpoint("http://wildfhir.aegis.net/fhir");
  //   context.registerDataProvider("http://hl7.org/fhir", provider);
  //
  //   xmlFile = new File(URLDecoder.decode(TestFhirLibrary.class.getResource("measure-cbp.xml").getFile(), "UTF-8"));
  //   Measure measure = provider.getFhirClient().getFhirContext().newXmlParser().parseResource(Measure.class, new FileReader(xmlFile));
  //
  //   Patient patient = provider.getFhirClient().read().resource(Patient.class).withId("pat001").execute();
  //   // TODO: Couldn't figure out what matcher to use here, gave up.
  //   if (patient == null) {
  //       throw new RuntimeException("Patient is null");
  //   }
  //
  //   context.setContextValue("Patient", patient.getId());
  //
  //   FhirMeasureEvaluator evaluator = new FhirMeasureEvaluator();
  //   org.hl7.fhir.dstu3.model.MeasureReport report = evaluator.evaluate(provider.getFhirClient(), context, measure, patient);
  //
  //   if (report == null) {
  //       throw new RuntimeException("MeasureReport is null");
  //   }
  //
  //   if (report.getEvaluatedResources() == null) {
  //       throw new RuntimeException("EvaluatedResources is null");
  //   }
  // }
}
