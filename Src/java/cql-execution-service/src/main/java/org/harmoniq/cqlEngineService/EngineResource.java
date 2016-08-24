package org.harmoniq.cqlEngineService;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
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
import org.cqframework.cql.cql2elm.FhirModelInfoProvider;
import org.cqframework.cql.cql2elm.ModelInfoLoader;

import org.hl7.elm.r1.VersionedIdentifier;

import org.cqframework.cql.elm.tracking.TrackBack;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.execution.Context;
import org.cqframework.cql.execution.CqlLibraryReader;

import org.cqframework.cql.data.fhir.FhirDataProvider;
import org.cqframework.cql.data.fhir.FhirMeasureEvaluator;

import org.hl7.fhir.dstu3.model.Measure;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.MeasureReport;

import org.hl7.fhir.dstu3.model.Bundle;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;

import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

@Path("evaluate")
public class EngineResource {

  Library library = null;
  private File xmlFile = null;
  private String measurePath = null;

  @POST
  @Consumes({MediaType.TEXT_PLAIN})
  @Produces({MediaType.TEXT_PLAIN})
  public String evaluateCql(String cql) throws JAXBException, IOException {
    Map<String, Integer> expressionNameAndLocMap = getNameAndLocMap(cql);
    JSONArray resultArr = new JSONArray();
    try {
      translate(cql);
    }
    catch (IllegalArgumentException e) {
      JSONObject results = new JSONObject();
      results.put("translation-error", e.getMessage());
      resultArr.add(results);
      return resultArr.toJSONString();
    }

    // get results and build response
    Context context = new Context(library);
    for (String key : expressionNameAndLocMap.keySet()) {
      JSONObject results = new JSONObject();
      try {
        results.put("name", key);
        Object result = context.resolveExpressionRef(library, key).getExpression().evaluate(context);
        // making an assumption here that expression appears at begining column of line....
        results.put("location", "[" + expressionNameAndLocMap.get(key) + ": 1]");
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

  public Map<String, Integer> getNameAndLocMap(String cql) {
    // remove comments
    cql = cql.replaceAll("(//).*", "");
    cql = replaceMultilineComments(cql);

    // get each line
    String[] linesOfCode = cql.split("\\r?\\n");

    // Map format: { "name of expression" : location/line# of expression }
    Map<String, Integer> lineContents = new HashMap<>();
    for (int i = 0; i < linesOfCode.length; ++i) {
      // get define statement, but NOT functions
      if (linesOfCode[i].indexOf("define") >= 0 && linesOfCode[i].indexOf("define function") == -1) {
        Matcher m = Pattern.compile("(?<=define\\s).*?(?=:)").matcher(linesOfCode[i]);
        while (m.find()) {
          // line number = i + 1
          lineContents.put(m.group().replaceAll("\"", "").trim(), i + 1);
        }
      }
    }
    return lineContents;
  }

  public String replaceMultilineComments(String cql) {
    int numLines = 0;
    int idx = 0;
    while (cql.indexOf("/*", idx) >= 0) {
      String commented = cql.substring(cql.indexOf("/*", idx), cql.indexOf("*/", idx + 1) + 2);
      numLines += commented.split("\\r?\\n").length;
      String replace = "";
      for (int i = 0; i < numLines - 1; ++i) {
        replace += "\n";
      }
      cql = cql.replace(commented, replace);
      idx = cql.indexOf("*/", idx);
    }
    return cql;
  }

  public void translate(String cql) throws JAXBException, IOException {
    try {
      ArrayList<CqlTranslator.Options> options = new ArrayList<>();
      options.add(CqlTranslator.Options.EnableDateRangeOptimization);
      CqlTranslator translator = CqlTranslator.fromText(cql, new LibraryManager(), options.toArray(new CqlTranslator.Options[options.size()]));

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
}
