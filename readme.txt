Se inyecta webDriver en los test. 

Propiedades:
- Los test unitarios no se ejecutarán en paralelo (no se puede abrir más de un navegador).  
- Al iniciar webDriver hace login y se situa sobre la página principal.
- Antes de ejecutar un test se situa sobre la página principal.
- Al acabar la suite se desconecta. Si se está ejecutando un test directamente, se desconectará al finalizar el mismo.

Parametrizaciones:
- La url base
- Prametros de qué clase java implementa la secuencia de login (por si hubiesen diferentes logins)
- La url pgina de login y los valores de usuario, contraseña...

Parametrizaciones a 3 niveles (ordenados de mayor importancia a menor): 
- VM: VM params-> -Dpages.urlHome=2009/09/we-have-web-project-that-90-of-data-its.html
- Ficheros de configuración a nivel de suite de ejecución.
- Ficheros de configuración por defecto de la API.

---------------------------------------------------------------------
---------------------------------------------------------------------
Explicación del funcionamiento de la API:
Con ISuiteListener y ITestListener puedo controlar cuando se inica/finaliza un test o una suite de esta forma controlo la inyección de
webDriver, de iniciarlo y pararlo.


https://groups.google.com/forum/#!topic/selenium-users/cFjQdrf3Xhg
http://www.youtube.com/watch?v=6vPu3TO6XZ4&feature=player_embedded

https://github.com/dev9com/junit-webdriver
webdriver {
    browser:    htmlunit
    type:       local
}

//This profile would be triggered by setting the system property 'webtest.profile' to 'local-firefox'
//For example: mvn verify -Dwebtest.profile=local-firefox
local-firefox {
    webdriver {
        browser:    firefox
        type:       local
    }
}

//Remote browsers are run in sauce labs.  You'll need to have a couple of environment variables set for your credentials.
//SAUCELABS_USER - your Sauce Labs user name
//SAUCELABS_KEY - your Sauce Labs key
remote-chrome {
    webdriver {
        browser:    chrome
        type:       remote
        version:    ""
        platform:   linux
    }
}

public class DataDrivenUsingFactories implements ITest {
    private String name;
    private int age;
 
    @Factory(dataProvider = "dp")
    public DataDrivenUsingFactories(String name, int age) {
        this.name = name;
        this.age = age;
    }
 
    @Test
    public void testName() {
        assertNotNull(name, "Name validation successful");
    }
 
    @Test
    public void testAge() {
        assertTrue(age != 0, "Age validation successful");
    }
 
    @DataProvider
    public static Object[][] dp() {
        return new Object[][] { { "John", 30 }, { "Rambo", 40 } };
    }
 
    public String getTestName() {
        StringBuilder builder = new StringBuilder();
        builder.append("[name=").append(name).append(", age=").append(age).append("]");
        return builder.toString();
    }
}

The convenience method ConfigFactory.load() loads the following (first-listed are higher priority):

system properties
application.conf (all resources on classpath with this name)
application.json (all resources on classpath with this name)
application.properties (all resources on classpath with this name)
reference.conf (all resources on classpath with this name)

Dbrowser=firefox
-Dbrowser=chrome
-Dbrowser=ie
-Dbrowser=opera
-Dbrowser=htmlunit
-Dbrowser=ghostdriver

http://www.emforge.net/web/first-test-project/sources;jsessionid=23075FF65ED2A91A07E5F595C0BEA46A?p_p_id=scmportlet_WAR_emforgeportlet&p_p_lifecycle=0&p_p_state=maximized&p_p_mode=view&_scmportlet_WAR_emforgeportlet_path=bi-platform-webdriver-tests%2Ftrunk%2Fsrc%2Forg%2Fpentaho%2Fwebdriver%2Fmodel%2Fpuc%2Ftools%2FMainMenu.java&_scmportlet_WAR_emforgeportlet_version=
-Dwebdriver.exe.ff="C:/apps/firefox/firefox.exe"
-Dwebdriver.browser="IEXPLORE"
-Dwebdriver.browser="FIREFOX" -Duser.username="84336" -Duser.pc="87" -Duser.pasword.ims="imsbcn" -Duser.pasword.ags="agsbcn" -Dpages.urlBase="https://pre01.tf7.s2o.es/"
-Dwebdriver.seconds_implicitly_wait=20
-Dwebdriver.browser="IEXPLORE" -Dwebdriver.exe.ie="C:/apps/selenium/IEDriverServer.exe" -Dwebdriver.proxy.type="DIRECT"   	


clean install -U -e -Dwebdriver.browser="FIREFOX" -Dwebdriver.exe.ff="C:/apps/firefox/firefox.exe" -Duser.username="84336" -Duser.pc="87" -Duser.pasword.ims="imsbcn" -Duser.pasword.ags="agsbcn" -Dpages.urlBase="https://pre01.tf7.s2o.es/" -Dwebdriver.exe.ff="C:/apps/firefox/firefox.exe"
clean install -U -e -Dwebdriver.browser="IEXPLORE" -Dwebdriver.exe.ie="C:/apps/selenium/IEDriverServer.exe" -Duser.username="84336" -Duser.pc="87" -Duser.pasword.ims="imsbcn" -Duser.pasword.ags="agsbcn" -Dpages.urlBase="https://pre01.tf7.s2o.es/" -Dwebdriver.exe.ff="C:/apps/firefox/firefox.exe"



Ejecutar un test:
mvn -Dtest=TestCircle test
mvn -Dtest=TestSquare,TestCi*le test
mvn -Dtest=TestCircle#mytest test
mvn -Dtest=TestCircle#test* test