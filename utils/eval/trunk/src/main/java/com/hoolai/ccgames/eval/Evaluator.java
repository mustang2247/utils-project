package com.hoolai.ccgames.eval;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hoolai on 2016/7/30.
 * 参考http://onedear.iteye.com/blog/1158760
 * 如果不需要引入别的包，则调用evalBody即可，例如：
 *   System.out.println( 1234 + 5678 ); return "123";
 * 如果需要引入别的包，则需要调用eval，写上完整的类代码，并且提供一个public Object eval()方法，例如
 *   import com.hoolai.ccgames.xxx.xxx;
 *   public class ABC {
 *       public Object eval() {
 *           your code
 *       }
 *   }
 */
public class Evaluator {

    private static final Logger logger = LoggerFactory.getLogger( Evaluator.class );
    private static final String tmpFolder = System.getProperty( "java.io.tmpdir" );

    protected static String getClassCode( String className, String code ) {
        StringBuffer sb = new StringBuffer();
        sb.append( "public class " ).append( className ).append( " {" )
                .append( "public Object eval() {" )
                .append( code )
                .append( "}" )
                .append( "}" );
        return sb.toString();
    }

    public static void main( String[] args ) {
        Evaluator evaluator = new Evaluator();
        try {
//            String classCode = "public class Abc_Abc  {";
//            Pattern pattern = Pattern.compile( "public[ \t]+class[ \\t]+([A-Za-z_]+)[ \t]+\\{", Pattern.MULTILINE | Pattern.UNIX_LINES );
//            Matcher matcher = pattern.matcher( classCode );
//            if( matcher.find() ) {
//                System.out.println( "FOUND" );
//                for( int i = 0; i <= matcher.groupCount(); i++ ) {
//                    System.out.println( matcher.group( i ) );
//                }
//            }
//            else {
//                System.out.println( "NOT FOUND" );
//            }
            System.out.println( evaluator.evalBody( "System.out.println(\"e3111112222222\"); return \"1\";" ) );
            System.out.println( evaluator.eval( "import com.hoolai.ccgames.eval.hello;\n" +
                    "public class ABC_abc {\n" +
                    "public Object eval() {\n" +
                    "System.out.println(\"e3111112222222\"); return \"1\";" +
                    "}}"
            ) );
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
    }

    public Object evalBody( String funcCode ) throws Exception {
        return eval( getClassCode( "MyEval", funcCode ) );
    }

    public Object eval( String sourceCode )
            throws SecurityException, NoSuchMethodException, IOException,
            ClassNotFoundException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, InstantiationException {

        // 获取基础构件
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if( compiler == null ) {
            logger.error( "[Evaluator::eval] Can't find java compiler, ignore task" );
            return null;
        }
        DiagnosticCollector diagnostics = new DiagnosticCollector();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager( diagnostics, null, null );

        // 生成java文件
        String classCode = sourceCode;
        Pattern pattern = Pattern.compile( "public[ \t]+class[ \\t]+([A-Za-z0-9_]+)[ \t]+\\{", Pattern.MULTILINE | Pattern.UNIX_LINES );
        Matcher matcher = pattern.matcher( classCode );
        if( !matcher.find() ) {
            logger.error( "[Evaluator::eval] Can't extract class name, ignore task" );
            return null;
        }
        String className = matcher.group( 1 );

        File file = new File( tmpFolder, className + ".java" );
        PrintWriter pw = new PrintWriter( file );
        pw.println( classCode );
        pw.close();

        // 编译java文件，生成class文件
        Iterable compilationUnits = fileManager.getJavaFileObjectsFromStrings( Arrays.asList( file
                .getAbsolutePath() ) );
        JavaCompiler.CompilationTask task = compiler.getTask( null,
                fileManager, diagnostics, Arrays.asList( "-XDuseUnsharedTable=true" ), null, compilationUnits );
        boolean success = task.call();
        if( !success ) {
            StringBuilder sb = new StringBuilder();
            diagnostics.getDiagnostics().forEach( d -> sb.append( d.toString() ) );
            logger.error( "[Evaluator::eval] compile fail, please check ur code\n{}\n", sb.toString() );
            file.delete();
            return null;
        }
        fileManager.close();

        // 加载类文件到内存，提取方法
        URLClassLoader classLoader = new URLClassLoader(
                new URL[]{ new File( tmpFolder ).toURI().toURL() } );
        Class clazz = classLoader.loadClass( className );
        Method method = clazz.getDeclaredMethod( "eval" );

        // 删除java和class文件
        file.delete();
        new File( tmpFolder, className + ".class" ).delete();

        // 调用方法
        return method.invoke( clazz.newInstance() );
    }
}
