package coop.magnesium.potassium.api;

import coop.magnesium.potassium.db.dao.RegistroDao;
import coop.magnesium.potassium.db.dao.RubroDao;
import coop.magnesium.potassium.db.dao.TareaDao;
import coop.magnesium.potassium.db.dao.UsuarioDao;
import coop.magnesium.potassium.db.entities.Registro;
import coop.magnesium.potassium.db.entities.Rubro;
import coop.magnesium.potassium.db.entities.Tarea;
import coop.magnesium.potassium.db.entities.Usuario;
import coop.magnesium.potassium.utils.Logged;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

/**
 * Created by pablo on 25/01/18.
 */
@RunWith(Arquillian.class)
public class RegistroServiceTest {

    @Inject
    Logger logger;

    @Inject
    RegistroDao registroDao;

    @Inject
    TareaDao tareaDao;

    final Tarea tarea1 = new Tarea("T1","D1", 120, 1);
    final Tarea tarea2 = new Tarea("T2","D2", 180, 1);


    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        File[] libs = Maven.resolver()
                .loadPomFromFile("pom.xml").resolve("com.fasterxml.jackson.datatype:jackson-datatype-jsr310").withTransitivity().asFile();
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, Filters.exclude(".*Test.*"),
                        Registro.class.getPackage(),
                        RegistroDao.class.getPackage(),
                        Logged.class.getPackage(),
                        Tarea.class.getPackage(),
                        TareaDao.class.getPackage(),
                        Usuario.class.getPackage(),
                        UsuarioDao.class.getPackage(),
                        Rubro.class.getPackage(),
                        RubroDao.class.getPackage())
                .addClass(JAXRSConfiguration.class)
                .addClass(RegistroService.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("endpoints.properties")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsLibraries(libs);
    }

    @Test
    @InSequence(1)
    public void inicializarBd() {
//        tareaDao.save(this.tarea1);
//        tareaDao.save(this.tarea2);
    }


//    @Test
//    @InSequence(2)
//    @RunAsClient
//    public void createRegistro(@ArquillianResteasyResource final WebTarget webTarget) {
//        this.tarea1.setId(1L);
//        final Response response = webTarget
//                .path("/registros")
//                .request(MediaType.APPLICATION_JSON)
//                .post(Entity.json(new Registro(120, this.tarea1, LocalDate.now())));
//        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
//    }
//
//    @Test
//    @InSequence(3)
//    @RunAsClient
//    public void createRegistroSinTarea(@ArquillianResteasyResource final WebTarget webTarget) {
//        Registro registro = new Registro();
//        registro.setMinutos(120);
//        registro.setFecha(LocalDate.now());
//        final Response response = webTarget
//                .path("/registros")
//                .request(MediaType.APPLICATION_JSON)
//                .post(Entity.json(registro));
//        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
//    }

}
