package coop.magnesium.sulfur.api;

import coop.magnesium.sulfur.api.dto.HorasProyectoTipoTareaXCargo;
import coop.magnesium.sulfur.api.dto.HorasProyectoXCargo;
import coop.magnesium.sulfur.db.dao.CargoDao;
import coop.magnesium.sulfur.db.dao.EstimacionDao;
import coop.magnesium.sulfur.db.dao.ProyectoDao;
import coop.magnesium.sulfur.db.dao.TipoTareaDao;
import coop.magnesium.sulfur.db.entities.*;
import coop.magnesium.sulfur.utils.Logged;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

/**
 * Created by rsperoni on 20/11/17.
 */
@RunWith(Arquillian.class)
public class EstimacionServiceTest {

    final Proyecto proyecto = new Proyecto("PP", "PP");
    final TipoTarea tipoTarea = new TipoTarea("TT", "TT");
    final Cargo cargo = new Cargo("CC", "CC", new BigDecimal(32.2));

    @Inject
    CargoDao cargoDao;
    @Inject
    ProyectoDao proyectoDao;
    @Inject
    TipoTareaDao tipoTareaDao;
    @Inject
    Logger logger;


    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        File[] libs = Maven.resolver()
                .loadPomFromFile("pom.xml").resolve("com.fasterxml.jackson.datatype:jackson-datatype-jsr310").withTransitivity().asFile();
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, Filters.exclude(".*Test.*"),
                        Estimacion.class.getPackage(),
                        EstimacionDao.class.getPackage(),
                        Logged.class.getPackage(),
                        HorasProyectoXCargo.class.getPackage())
                .addClass(JAXRSConfiguration.class)
                .addClass(EstimacionService.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("endpoints.properties")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsLibraries(libs);
    }

    @Before
    public void init() {
        this.proyecto.setId(1L);
        this.tipoTarea.setId(1L);
        this.cargo.setId(1L);
    }

    @Test
    @InSequence(1)
    public void inicializarBd() {
        logger.info(proyectoDao.save(this.proyecto).toString());
        logger.info(tipoTareaDao.save(this.tipoTarea).toString());
        logger.info(cargoDao.save(this.cargo).toString());
    }

    @Test
    @InSequence(2)
    @RunAsClient
    public void createEstmacion(@ArquillianResteasyResource final WebTarget webTarget) {
        final Response response = webTarget
                .path("/estimaciones")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(new Estimacion(this.proyecto, null, LocalDate.now())));
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        Estimacion estimacion = response.readEntity(Estimacion.class);
        assertEquals(1, estimacion.getId().longValue());
    }

    @Test
    @InSequence(3)
    @RunAsClient
    public void createEstmacion2(@ArquillianResteasyResource final WebTarget webTarget) {
        Estimacion estimacion = new Estimacion(this.proyecto, null, LocalDate.now());
        estimacion.getEstimacionDetalleList().add(new EstimacionDetalle(this.tipoTarea, this.cargo, Duration.ofHours(3), new BigDecimal(150.5)));
        estimacion.getEstimacionDetalleList().add(new EstimacionDetalle(this.tipoTarea, this.cargo, Duration.ofHours(6), new BigDecimal(170)));
        final Response response = webTarget
                .path("/estimaciones")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(estimacion));
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        Estimacion estimacionCreated = response.readEntity(Estimacion.class);
        assertEquals(2, estimacionCreated.getId().longValue());
    }

    //Todo: servicios y tests de consultas sobre las estimaciones.


}