package utils;

import org.apache.log4j.Logger;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;

public class CreditRequestValidator {
  private static final Logger log = Logger.getLogger(CreditRequestValidator.class);

  public static boolean isValid(String json, String pathToSchema) {
    try (InputStream inputStream = CreditRequestValidator.class.getResourceAsStream(pathToSchema)) {
      JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
      Schema schema = SchemaLoader.load(rawSchema);
      schema.validate(new JSONObject(json));
    } catch (IOException e) {
      log.error("JSON Schema reading error: " + e);
      return false;
    } catch (ValidationException e) {
      log.error(e);
      return false;
    }
    return true;
  }
}
