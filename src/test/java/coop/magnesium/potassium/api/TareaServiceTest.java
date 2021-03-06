package coop.magnesium.potassium.api;

import static org.junit.Assert.assertEquals;

/**
 * Created by rsperoni on 20/11/17.
 */
//@RunWith(Arquillian.class)
public class TareaServiceTest {
//    @Deployment(testable = false)
//    public static WebArchive createDeployment() {
//        return ShrinkWrap.create(WebArchive.class)
//                .addPackages(true, Filters.exclude(".*Test.*"),
//                        TipoTarea.class.getPackage(),
//                        TipoTareaDao.class.getPackage(),
//                        Logged.class.getPackage(),
//                        HorasProyectoXCargo.class.getPackage())
//                .addClass(JAXRSConfiguration.class)
//                .addClass(TipoTareaService.class)
//                .addAsResource("META-INF/persistence.xml")
//                .addAsResource("endpoints.properties")
//                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
//    }
//
//    @Test
//    @InSequence(1)
//    @RunAsClient
//    public void createTarea(@ArquillianResteasyResource final WebTarget webTarget) {
//        final Response response = webTarget
//                .path("/tareas")
//                .request(MediaType.APPLICATION_JSON)
//                .post(Entity.json(new TipoTarea("TT1", "Tipo tarea 1")));
//        assertEquals(1, response.readEntity(TipoTarea.class).getId().longValue());
//    }
//
//    @Test
//    @InSequence(2)
//    @RunAsClient
//    public void createTareaCodigoExiste(@ArquillianResteasyResource final WebTarget webTarget) {
//        final Response response = webTarget
//                .path("/tareas")
//                .request(MediaType.APPLICATION_JSON)
//                .post(Entity.json(new TipoTarea("TT1", "Tipo tarea 2")));
//        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
//    }
//
//    @Test
//    @InSequence(3)
//    @RunAsClient
//    public void createTareaIdExiste(@ArquillianResteasyResource final WebTarget webTarget) {
//        TipoTarea tipoTarea = new TipoTarea("TT1", "Tipo tarea 2");
//        tipoTarea.setId(1L);
//        final Response response = webTarget
//                .path("/tareas")
//                .request(MediaType.APPLICATION_JSON)
//                .post(Entity.json(tipoTarea));
//        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
//    }
//
//    @Test
//    @InSequence(4)
//    @RunAsClient
//    public void getTareas(@ArquillianResteasyResource final WebTarget webTarget) {
//        final Response response = webTarget
//                .path("/tareas")
//                .request(MediaType.APPLICATION_JSON)
//                .get();
//        List<TipoTarea> tareaList = response.readEntity(new GenericType<List<TipoTarea>>() {
//        });
//        assertEquals(1, tareaList.size());
//    }
//
//    @Test
//    @InSequence(5)
//    @RunAsClient
//    public void getTarea(@ArquillianResteasyResource final WebTarget webTarget) {
//        final Response response = webTarget
//                .path("/tareas/1")
//                .request(MediaType.APPLICATION_JSON)
//                .get();
//        TipoTarea tarea = response.readEntity(TipoTarea.class);
//        assertEquals(1L, tarea.getId().longValue());
//        assertEquals("TT1", tarea.getCodigo());
//        assertEquals("Tipo tarea 1", tarea.getNombre());
//    }
//
//    @Test
//    @InSequence(5)
//    @RunAsClient
//    public void getTareaNoExiste(@ArquillianResteasyResource final WebTarget webTarget) {
//        final Response response = webTarget
//                .path("/tareas/4")
//                .request(MediaType.APPLICATION_JSON)
//                .get();
//        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
//    }
//
//    @Test
//    @InSequence(6)
//    @RunAsClient
//    public void editarTareaExiste(@ArquillianResteasyResource final WebTarget webTarget) {
//        TipoTarea tarea = new TipoTarea("TT1111", "Tipo tarea 1 ok");
//        tarea.setId(1L);
//        final Response response = webTarget
//                .path("/tareas/1")
//                .request(MediaType.APPLICATION_JSON)
//                .put(Entity.json(tarea));
//        Cargo returned = response.readEntity(Cargo.class);
//        assertEquals(tarea.getCodigo(), returned.getCodigo());
//        assertEquals(tarea.getNombre(), returned.getNombre());
//    }
//
//    @Test
//    @InSequence(6)
//    @RunAsClient
//    public void editarTareaNoExiste(@ArquillianResteasyResource final WebTarget webTarget) {
//        TipoTarea tarea = new TipoTarea("TT1111", "Tipo tarea 1 ok");
//        tarea.setId(4L);
//        final Response response = webTarget
//                .path("/tareas/4")
//                .request(MediaType.APPLICATION_JSON)
//                .put(Entity.json(tarea));
//        assertEquals(Response.Status.NOT_MODIFIED.getStatusCode(), response.getStatus());
//    }
//

}
