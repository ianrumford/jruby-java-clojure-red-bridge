/*

  Runs a JRuby script using Redbridge Embedded Core

*/

package name.rumford;

import java.io.IOException;

// Java basics
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.lang.Class;

// JRuby Scripting Container
import org.jruby.embed.ScriptingContainer;

// Java IO
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.Reader;

// OptionParser
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;


// Logger
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaJRubyRedBridge
{

  /**
   * Manages Ruby Script Exceution
   *
   */

  protected final Logger logger = LoggerFactory.getLogger(JavaJRubyRedBridge.class);

  public static void main(String[] args) throws IOException {

    /*

      A very simple test of passing a HashMap and expecting one one back

    */

    HashMap<String,String> methodArgs = new HashMap<String,String>();

    JavaJRubyRedBridge redBridge = new JavaJRubyRedBridge(args);

    methodArgs.put("method", redBridge.getRubyScriptMethod());
    methodArgs.put("path", redBridge.getRubyScriptPath());

    redBridge.callRubyMethod(redBridge.getRubyScriptMethod(),
                             methodArgs,
                             HashMap.class);

  }

  public JavaJRubyRedBridge() throws IOException {
  }

  public JavaJRubyRedBridge(String[] udfArgs) throws IOException {

    if (udfArgs.length < 1) {
      throw new IOException("JJRB::constructor NO ARGS SUPPLIED");
    } else {
      processArguments(udfArgs);
    }

  }


  private void processArguments(String[] udfArgs) throws IOException {

    logger.info("JJRB::processArguments ==> UDF ARGS >{}<", udfArgs.length);

    for (String udfArg : udfArgs) {
      logger.info("JJRB::processArguments ARG >{}<", udfArg);
    }

    OptionParser udfArgParser = new OptionParser();

    List<String> synonymsRubyScriptPath = Arrays.asList("ruby-script", "ruby-path", "path");
    OptionSpec<String> specRubyScriptPath = udfArgParser.acceptsAll(synonymsRubyScriptPath).withRequiredArg();

    List<String> synonymsRubyScriptMethod = Arrays.asList("ruby-method", "method");
    OptionSpec<String> specRubyScriptMethod = udfArgParser.acceptsAll(synonymsRubyScriptMethod).withRequiredArg();

    //OptionSet udfArgOptions = udfArgParser.parse(udfArgString);
    OptionSet udfArgOptions = udfArgParser.parse(udfArgs);

    // Ruby Script Path
    if (udfArgOptions.has(specRubyScriptPath)) {
      setRubyScriptPath(udfArgOptions.valueOf(specRubyScriptPath));
    }

    // Ruby Script Method
    if (udfArgOptions.has(specRubyScriptMethod)) {
      setRubyScriptMethod(udfArgOptions.valueOf(specRubyScriptMethod));
    }

    logger.info("JJRB::processArguments <== UDF ARGS >{}<", udfArgs.length);

    //initFlag = true;

  }


  // CALL RUBY METHOD
  // ****************


  public <T> T callRubyMethod(String methodName,
                              Object[] methodArgs,
                              Class<T> resultClass) throws IOException {

    logger.info("JJRB::callRubyMethod ==>  METHOD >{}< DATA >{}< ", methodName, methodArgs.length);

    T scriptResult = callScriptingMethod(methodName,
                                         methodArgs, 
                                         resultClass);

    logger.info("JJRB::callRubyMethod <==  METHOD >{}< RESULT >{}< >{}<", methodName, scriptResult.getClass().getName(), scriptResult);

    return scriptResult;

  }

  // support list of args (c.f. array)
  public <T> T callRubyMethod(String methodName,
                              List<Object> methodArgs,
                              Class<T> returnClass
                              ) throws IOException {

    return callRubyMethod(methodName,
                          methodArgs.toArray(new Object[methodArgs.size()]),
                          returnClass);
  }

  public <T> T callRubyMethod(String methodName,
                              Object methodArg,
                              Class<T> returnClass
                              ) throws IOException {

    Object[] methodArgs = {methodArg};

    return callRubyMethod(methodName,
                          methodArgs,
                          returnClass);
  }


  /*

    SCRIPTING CONTAINER
    *******************

    The container is the instance of the Ruby interpreter

  */

  private ScriptingContainer valueScriptingContainer = null;

  public ScriptingContainer newScriptingContainer() throws IOException {

    ScriptingContainer newContainer = new ScriptingContainer();

    logger.info("JJRB::newScriptingContainer <=> NEW CONTAINER >{}<", newContainer);

    return newContainer;

  }

  public ScriptingContainer getScriptingContainer() throws IOException {
    if (valueScriptingContainer == null) {
      throw new IOException("SCRIPTING CONTAINER IS NULL");
    }
    return valueScriptingContainer;
  }

  public void setScriptingContainer(ScriptingContainer newContainer)  {
    valueScriptingContainer = newContainer;
  }

 public boolean haveScriptingContainer()  {
   return (valueScriptingContainer != null) ? true : false;
 }

  public ScriptingContainer findScriptingContainer() throws IOException {
    if (! haveScriptingContainer()) {
      setScriptingContainer(newScriptingContainer());
    }
    return getScriptingContainer();
  }

  /*

    SCRIPTING RECEIVER
    ******************

    The scipting receiver is the Ruby instance that will run the method

    Its an instance of a Ruby class

  */

  private Object valueScriptingReceiver = null;

  public Object newScriptingReceiver(String scriptPath) throws IOException {

    logger.info("JJRB::newScriptingReceiver ==> SCRIPT PATH >{}<", scriptPath);

    Object scriptReceiver;

    try {
      Reader scriptReader = newScriptingReader(scriptPath);

      scriptReceiver = getScriptingContainer().runScriptlet(scriptReader, scriptPath);

    } catch (IOException e) {

      e.printStackTrace(System.out);  // need a stack trace for debug

      logger.info("JJRB::newScriptingReceiver <== FAILED SCRIPT PATH >{}<", scriptPath);

      throw e;
    }

    logger.info("JJRB::newScriptingReceiver <== SCRIPT PATH >{}< RECEIVER >{}<", scriptPath, scriptReceiver);

    return scriptReceiver;

  }


  public Object getScriptingReceiver() throws IOException {
    if (valueScriptingReceiver == null) {
      throw new IOException("SCRIPTING RECEIVER IS NULL");
    }
    return valueScriptingReceiver;
  }

  public void setScriptingReceiver(Object newReceiver)  {
    valueScriptingReceiver = newReceiver;
  }

  public boolean haveScriptingReceiver()  {
    return (valueScriptingReceiver != null) ? true : false;
  }

  public Object findScriptingReceiver() throws IOException {
    if (! haveScriptingReceiver()) {
      setScriptingReceiver(newScriptingReceiver(getRubyScriptPath()));
    }
    return getScriptingReceiver();
  }


  /*

    RUBY SCRIPT READER
    ******************

    Provides a Reader to Redbridge to parse the script

  */

  public Reader newScriptingReader(String scriptPath) throws IOException {

   Reader scriptReader = new BufferedReader(new FileReader(scriptPath));

   logger.info("JJRB::newScriptingReader <=> SCRIPT PATH >{}< READER >{}<", scriptPath, scriptReader);

   return scriptReader;

  }


  /*

    RUBY SCRIPT PATH
    ****************

    Filesystem path to ruby script

  */

  private String valueRubyScriptPath = null;

  public String getRubyScriptPath() throws IOException {
    if (valueRubyScriptPath == null) {
      throw new IOException("SCRIPTING PATH IS NULL");
    }
    return valueRubyScriptPath;
  }

  public void setRubyScriptPath(String newPath)  {
    valueRubyScriptPath = newPath;
  }

 public boolean haveRubyScriptPath()  {
   return (valueRubyScriptPath != null) ? true : false;
 }


  /*

    RUBY SCRIPT METHOD
    ******************

    Filesystem method to ruby script

  */

  private String valueRubyScriptMethod = null;

  public String getRubyScriptMethod() throws IOException {
    if (valueRubyScriptMethod == null) {
      throw new IOException("RUBY METHOD IS NULL");
    }
    return valueRubyScriptMethod;
  }

  public void setRubyScriptMethod(String newMethod)  {
    valueRubyScriptMethod = newMethod;
  }

 public boolean haveRubyScriptMethod()  {
   return (valueRubyScriptMethod != null) ? true : false;
 }

  /*

    SCRIPTING METHOD
    ****************

    Calling via Redbridge

  */

  public <T> T callScriptingMethod(String methodName,
                                   Object[] methodArgs,
                                   Class<T> resultClass
                                   ) throws IOException {

    logger.info("JJRB::callScriptingMethod ==> METHOD >{}< ARGS >{}<", methodName, methodArgs.length);

    T resultValue = null;

    if (methodArgs.length > 0) {
      resultValue = findScriptingContainer().callMethod(findScriptingReceiver(), 
                                                        methodName,
                                                        methodArgs,
                                                        resultClass
                                                        );

    } else {
      resultValue = findScriptingContainer().callMethod(findScriptingReceiver(), 
                                                        methodName,
                                                        resultClass
                                                        );
    }


    if (resultValue != null) {
      logger.info("JJRB::callScriptingMethod <== METHOD >{}< RESULT >{}< >{}<", methodName, resultValue.getClass().getName(), resultValue);
    } else {
      logger.info("JJRB::callScriptingMethod <== METHOD >{}< RESULT IS NULL", methodName);
    }

    return resultValue;

  }



}


























