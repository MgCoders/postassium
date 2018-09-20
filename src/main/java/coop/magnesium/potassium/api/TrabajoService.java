package coop.magnesium.potassium.api;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import coop.magnesium.potassium.api.utils.JWTTokenNeeded;
import coop.magnesium.potassium.api.utils.RoleNeeded;
import coop.magnesium.potassium.db.dao.*;
import coop.magnesium.potassium.db.entities.*;
import coop.magnesium.potassium.utils.Logged;
import coop.magnesium.potassium.utils.PDFDocument;
import coop.magnesium.potassium.utils.ex.MagnesiumBdAlredyExistsException;
import coop.magnesium.potassium.utils.ex.MagnesiumBdNotFoundException;
import coop.magnesium.potassium.utils.ex.MagnesiumNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ejb.EJB;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by msteglich on 1/23/18.
 */
@Path("/trabajos")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
@Api(description = "Trabajos service", tags = "trabajos")
public class TrabajoService {

    @Inject
    private Logger logger;
    @EJB
    private TrabajoDao trabajoDao;
    @EJB
    private TrabajoFotoDao trabaFotojoDao;
    @EJB
    private EquipoDao equipoDao;
    @EJB
    private ClienteDao clienteDao;
    @EJB
    private PuntoControlDao puntoControlDao;
    @Inject
    Event<Notificacion> notificacionEvent;

    @POST
    @Logged
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Create Trabajo", response = Trabajo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 409, message = "Código o Id ya existe"),
            @ApiResponse(code = 500, message = "Error interno")})
    public Response create(@Valid Trabajo trabajo) {
        try {
            Trabajo trabajoExists = trabajo.getId() != null ? trabajoDao.findById(trabajo.getId()) : null;
            if (trabajoExists != null) throw new MagnesiumBdAlredyExistsException("Id ya existe");



            trabajo = trabajoDao.save(trabajo);

            String numeroTrabajo = "";

            if(trabajo.getEsReparacion()){
                numeroTrabajo += "OR";
            } else {
                numeroTrabajo += "OP";
            }

            if (trabajo.getId() < 10){ //Entre 1 y 9
                numeroTrabajo += "0000";
            } else if (trabajo.getId() < 100) //Entre 10 y 99
                numeroTrabajo += "000";
            else if (trabajo.getId() < 1000) //Entre 100 y 999
                numeroTrabajo += "00";
            else if (trabajo.getId() < 10000) //Entre 1000 y 9999
                numeroTrabajo += "0";

            numeroTrabajo += Long.toString(trabajo.getId());
            trabajo.setPorcentajeCompleto(0);
            trabajo.setNumeroTrabajo(numeroTrabajo);
            trabajoDao.save(trabajo);

            PuntoControl puntoControl  = new PuntoControl("Final", trabajo, 0, false);
            puntoControl = puntoControlDao.save(puntoControl);
            return Response.status(Response.Status.CREATED).entity(trabajo).build();
        } catch (MagnesiumBdAlredyExistsException exists) {
            logger.warning(exists.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(exists.getMessage()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get trabajos", response = Trabajo.class, responseContainer = "List")
    public Response findAll() {
        List<Trabajo> trabajoList = trabajoDao.findAll();
        return Response.ok(trabajoList).build();
    }

    @GET
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get Trabajo", response = Trabajo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Id no encontrado")})
    public Response find(@PathParam("id") Long id) {
        Trabajo trabajo = trabajoDao.findById(id);
        if (trabajo == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(trabajo).build();
    }

    @GET
    @Path("PDF/{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get PDF del trabajo.")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Id no encontrado")})
    public Response createPDF(@PathParam("id") Long id) throws DocumentException, IOException {

        // Buscamos el trabajo.
        Trabajo trabajo = trabajoDao.findById(id);        
        if (trabajo == null) return Response.status(Response.Status.NOT_FOUND).build();
        List<TrabajoFoto> fotos = trabaFotojoDao.findAllByTrabajo(id);

        // Creamos el Documento
        PDFDocument doc = new PDFDocument(0, "", "Ficha " + trabajo.getNumeroTrabajo(), true);

        // Titulo
        List<Paragraph> ph = new ArrayList<Paragraph>();
        Paragraph p = new Paragraph();
        Chunk ch = new Chunk("Trabajo ", doc.title_bold);
        p.add(ch);
        ch = new Chunk(trabajo.getNumeroTrabajo(), doc.title);
        p.add(ch);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        ph.add(p);        
        doc.addTituloContenido(ph, new Float(40));

        // Otros Datos **************************************

        // Cliente
        ph = new ArrayList<Paragraph>();
        p = new Paragraph();
        ch = new Chunk("Cliente: ", doc.f12b);
        p.add(ch);
        ch = new Chunk(trabajo.getCliente().getNombreEmpresa() + "         ", doc.f12);
        p.add(ch);
        ch = new Chunk("RUT: ", doc.f12b);
        p.add(ch);
        ch = new Chunk(trabajo.getCliente().getRut(), doc.f12);
        p.add(ch);
        ph.add(p);        
        doc.addTituloContenido(ph, new Float(20));

        if(trabajo.getEquipo() != null) {
            // Equipo
            ph = new ArrayList<Paragraph>();
            p = new Paragraph();
            ch = new Chunk("Matrícula Equipo: ", doc.f12b);
            p.add(ch);
            ch = new Chunk(trabajo.getEquipo().getMatricula() + "         ", doc.f12);
            p.add(ch);
            ch = new Chunk("Marca/Modelo Equipo: ", doc.f12b);
            p.add(ch);
            ch = new Chunk(trabajo.getEquipo().getMarca() + " / " + trabajo.getEquipo().getModelo(), doc.f12);
            p.add(ch);
            ph.add(p);
            doc.addTituloContenido(ph, new Float(20));
        }

        // Motivo de la visita.
        ph = new ArrayList<Paragraph>();
        p = new Paragraph();
        ch = new Chunk("Motivo de la visita: ", doc.f12b);
        p.add(ch);
        ch = new Chunk(trabajo.getMotivoVisita(), doc.f12);
        p.add(ch);
        ph.add(p);        
        doc.addTituloContenido(ph, new Float(20));

        // Fecha Recepción y fecha prevista de entrega
        DateTimeFormatter formatterRecepcion = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        DateTimeFormatter formatterEntrega = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        ph = new ArrayList<Paragraph>();
        p = new Paragraph();
        ch = new Chunk("Fecha de recepción: ", doc.f12b);
        p.add(ch);
        ch = new Chunk(trabajo.getFechaRecepcion().format(formatterRecepcion)+ "         ", doc.f12);
        p.add(ch);
        ch = new Chunk("Fecha de prevista de entrega: ", doc.f12b);
        p.add(ch);
        ch = new Chunk(trabajo.getFechaProvistaEntrega().format(formatterEntrega), doc.f12);
        p.add(ch);
        ph.add(p);
        doc.addTituloContenido(ph, new Float(20));

        // Número orden de compra y cotización
        ph = new ArrayList<Paragraph>();
        p = new Paragraph();
        ch = new Chunk("Orden de compra: ", doc.f12b);
        p.add(ch);
        ch = new Chunk(trabajo.getNroOrdenCompra()+ "         ", doc.f12);
        p.add(ch);
        ch = new Chunk("Cotización: ", doc.f12b);
        p.add(ch);
        ch = new Chunk(trabajo.getCotizacion(), doc.f12);
        p.add(ch);
        ph.add(p);
        doc.addTituloContenido(ph, new Float(20));



//
//        // Remito
//        ph = new ArrayList<Paragraph>();
//        p = new Paragraph();
//        ch = new Chunk("Remito: ", doc.f12b);
//        p.add(ch);
//        // TODO Como obtener el remito.
//        ch = new Chunk("N/A", doc.f12);
//        p.add(ch);
//        ph.add(p);
//        doc.addTituloContenido(ph, new Float(20));



        if(trabajo.getEquipo() != null) {
            // Datos Extra.
            ph = new ArrayList<Paragraph>();
            p = new Paragraph();
            if (trabajo.getEquipoAbollones() != null) {
                ch = new Chunk("Abollones: ", doc.f10b);
                p.add(ch);
                ch = new Chunk(trabajo.getEquipoAbollones() ? "Si" : "No", doc.f10);
                p.add(ch);
            }

            if (trabajo.getEquipoAuxiliar() != null) {
                ch = new Chunk("     Auxiliar: ", doc.f10b);
                p.add(ch);
                ch = new Chunk(trabajo.getEquipoAuxiliar() ? "Si" : "No", doc.f10);
                p.add(ch);
            }

            if (trabajo.getEquipoAuxiliarArmada() != null) {
                ch = new Chunk("     Auxiliar Armada: ", doc.f10b);
                p.add(ch);
                ch = new Chunk(trabajo.getEquipoAuxiliarArmada() ? "Si" : "No", doc.f10);
                p.add(ch);
            }

            if (trabajo.getEquipoBalizas() != null) {
                ch = new Chunk("     Balizas: ", doc.f10b);
                p.add(ch);
                ch = new Chunk(trabajo.getEquipoBalizas() ? "Si" : "No", doc.f10);
                p.add(ch);
            }

            if (trabajo.getEquipoCantidadCombustible() != null) {
                ch = new Chunk("     Cant. Combustible: ", doc.f10b);
                p.add(ch);
                ch = new Chunk(trabajo.getEquipoCantidadCombustible().toString(), doc.f10);
                p.add(ch);
            }

            if (trabajo.getEquipoCenicero() != null) {
                ch = new Chunk("     Cenicero: ", doc.f10b);
                p.add(ch);
                ch = new Chunk(trabajo.getEquipoCenicero() ? "Si" : "No", doc.f10);
                p.add(ch);
            }

            if (trabajo.getEquipoDocumentos() != null) {
                ch = new Chunk("     Documentos: ", doc.f10b);
                p.add(ch);
                ch = new Chunk(trabajo.getEquipoDocumentos() ? "Si" : "No", doc.f10);
                p.add(ch);
            }

            if (trabajo.getEquipoEspejos() != null) {
                ch = new Chunk("     Espejos: ", doc.f10b);
                p.add(ch);
                ch = new Chunk(trabajo.getEquipoEspejos() ? "Si" : "No", doc.f10);
                p.add(ch);
            }

            if (trabajo.getEquipoEspejosSanos() != null) {
                ch = new Chunk("     Espejos Sanos: ", doc.f10b);
                p.add(ch);
                ch = new Chunk(trabajo.getEquipoEspejosSanos() ? "Si" : "No", doc.f10);
                p.add(ch);
            }

            if (trabajo.getEquipoExtintor() != null) {
                ch = new Chunk("     Extintor: ", doc.f10b);
                p.add(ch);
                ch = new Chunk(trabajo.getEquipoExtintor() ? "Si" : "No", doc.f10);
                p.add(ch);
            }

            if (trabajo.getEquipoFrenteRadio() != null) {
                ch = new Chunk("     Frente de Radio: ", doc.f10b);
                p.add(ch);
                ch = new Chunk(trabajo.getEquipoFrenteRadio() ? "Si" : "No", doc.f10);
                p.add(ch);
            }

            if (trabajo.getEquipoGatoPalanca() != null) {
                ch = new Chunk("     Palanca de Gato: ", doc.f10b);
                p.add(ch);
                ch = new Chunk(trabajo.getEquipoGatoPalanca() ? "Si" : "No", doc.f10);
                p.add(ch);
            }

            if (trabajo.getEquipoHerramientas() != null) {
                ch = new Chunk("     Herramientas: ", doc.f10b);
                p.add(ch);
                ch = new Chunk(trabajo.getEquipoHerramientas() ? "Si" : "No", doc.f10);
                p.add(ch);
            }

            if (trabajo.getEquipoLlaveRuedas() != null) {
                ch = new Chunk("     Llave de Ruedas: ", doc.f10b);
                p.add(ch);
                ch = new Chunk(trabajo.getEquipoLlaveRuedas() ? "Si" : "No", doc.f10);
                p.add(ch);
            }

            if (trabajo.getEquipoLucesTraserasSanas() != null) {
                ch = new Chunk("     Luces Traseras Sanas: ", doc.f10b);
                p.add(ch);
                ch = new Chunk(trabajo.getEquipoLucesTraserasSanas() ? "Si" : "No", doc.f10);
                p.add(ch);
            }

            if (trabajo.getEquipoMangueraCabina() != null) {
                ch = new Chunk("     Manguera Cabina: ", doc.f10b);
                p.add(ch);
                ch = new Chunk(trabajo.getEquipoMangueraCabina() ? "Si" : "No", doc.f10);
                p.add(ch);
            }

            if (trabajo.getEquipoManuales() != null) {
                ch = new Chunk("     Manuales: ", doc.f10b);
                p.add(ch);
                ch = new Chunk(trabajo.getEquipoManuales() ? "Si" : "No", doc.f10);
                p.add(ch);
            }

            if (trabajo.getEquipoParabrisasSano() != null) {
                ch = new Chunk("     Parabrisas Sano: ", doc.f10b);
                p.add(ch);
                ch = new Chunk(trabajo.getEquipoParabrisasSano() ? "Si" : "No", doc.f10);
                p.add(ch);
            }

            if (trabajo.getEquipoRadio() != null) {
                ch = new Chunk("     Radio: ", doc.f10b);
                p.add(ch);
                ch = new Chunk(trabajo.getEquipoRadio() ? "Si" : "No", doc.f10);
                p.add(ch);
            }

            if (trabajo.getEquipoRayones() != null) {
                ch = new Chunk("     Rayones: ", doc.f10b);
                p.add(ch);
                ch = new Chunk(trabajo.getEquipoRayones() ? "Si" : "No", doc.f10);
                p.add(ch);
            }

            if (trabajo.getEquipoSenalerosSanos() != null) {
                ch = new Chunk("     Señaleros Sanos: ", doc.f10b);
                p.add(ch);
                ch = new Chunk(trabajo.getEquipoSenalerosSanos() ? "Si" : "No", doc.f10);
                p.add(ch);
            }

            if (trabajo.getEquipoVidriosLaterales() != null) {
                ch = new Chunk("     Vidrios Laterales: ", doc.f10b);
                p.add(ch);
                ch = new Chunk(trabajo.getEquipoVidriosLaterales() ? "Si" : "No", doc.f10);
                p.add(ch);
            }

            if (trabajo.getEquipoVidriosLateralesSanos() != null) {
                ch = new Chunk("     Vidrios Laterales Sanos: ", doc.f10b);
                p.add(ch);
                ch = new Chunk(trabajo.getEquipoVidriosLateralesSanos() ? "Si" : "No", doc.f10);
                p.add(ch);
            }
            ph.add(p);
            doc.addTituloContenido(ph, new Float(20));
        }



        // Agregamos las imagenes.
        for (Iterator<TrabajoFoto> i = fotos.iterator(); i.hasNext();) {
            doc.addSpace(30f);
            TrabajoFoto item = i.next();
            doc.addImageBase64(item.getDescripcion(), item.getFoto(), 0.60f, 0f);
        }

        // Agregamos las firmas.
        doc.addSpace(50f);
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100f);
        PdfPCell cell = new PdfPCell();
        if(trabajo.getFirmaClienteRecepcion() != null) {
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.addElement(doc.getImageFromBase64(trabajo.getFirmaClienteRecepcion()));
            Paragraph aux = new Paragraph(trabajo.getNombreClienteRecepcion() == null ? "" : trabajo.getNombreClienteRecepcion(), doc.f12b);
            aux.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(aux);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        }
        table.addCell(cell);        

        cell = new PdfPCell();
        if(trabajo.getFirmaEmpleadoRecepcion() != null) {
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.addElement(doc.getImageFromBase64(trabajo.getFirmaEmpleadoRecepcion()));
            Paragraph aux = new Paragraph(trabajo.getNombreEmpleadoRecepcion() == null ? "" : trabajo.getNombreEmpleadoRecepcion(), doc.f12b);
            aux.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(aux);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        }
        table.addCell(cell);
        doc.documento.add(table);

        // step 5
        doc.documento.close();

        Response.ResponseBuilder responseBuilder = Response.ok(doc.getBytes(), MediaType.APPLICATION_OCTET_STREAM);
        responseBuilder.header("Content-Disposition",  "attachment; filename=restfile.pdf");

        return responseBuilder.build();
    }

    @GET
    @Path("estado/{status}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get Trabajos", response = Trabajo.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Estado no encontrado")})
    public Response find(@PathParam("status") String status) {

        List<Trabajo> trabajoList = trabajoDao.findByEstado(status);
        return Response.ok(trabajoList).build();
    }

    @GET
    @Path("estados/{status}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get Trabajos", response = Trabajo.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Estado no encontrado")})
    public Response findByMultipleStatus(@PathParam("status") String status) {

        String[] estados = status.split(",");
        List<Trabajo> trabajoList = new ArrayList<>();
        for (String estado : estados){
            trabajoList.addAll(trabajoDao.findByEstado(estado));
        }


        return Response.ok(trabajoList).build();
    }

    @GET
    @Path("cliente/{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get Trabajos", response = Trabajo.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Cliente no encontrado")})
    public Response findByCliente(@PathParam("id") Long idCliente) {
        Cliente cliente = clienteDao.findById(idCliente);
        if (cliente == null) return Response.status(Response.Status.NOT_FOUND).build();

        List<Trabajo> trabajoList = trabajoDao.findAllByCliente(cliente);
        return Response.ok(trabajoList).build();
    }

    @GET
    @Path("equipo/{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get Trabajos", response = Trabajo.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Equipo no encontrado")})
    public Response findByEquipo(@PathParam("id") Long idEquipo) {
        Equipo equipo = equipoDao.findById(idEquipo);
        if (equipo == null) return Response.status(Response.Status.NOT_FOUND).build();

        List<Trabajo> trabajoList = trabajoDao.findAllByEquipo(equipo);
        return Response.ok(trabajoList).build();
    }

    @PUT
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Edit trabajo", response = Trabajo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "Error: objeto no modificado")})
    public Response edit(@PathParam("id") Long id, @Valid Trabajo trabajo) {
        try {
            Trabajo trabajo_old = trabajoDao.findById(id);
            if (trabajo_old == null) throw new MagnesiumNotFoundException("Trabajo no encontrado");

            // Chequeo de transiciones de estado.
            if (trabajo_old.getEstado().equals(Estado.EN_PROCESO.name()) &&
                    trabajo.getEstado().equals(Estado.PENDIENTE_REMITO.name())) {

                Notificacion notificacion = new Notificacion(TipoNotificacion.GENERAR_REMITO, trabajo.toNotificacion());
                notificacionEvent.fire(notificacion);
                Notificacion notificacion2 = new Notificacion(TipoNotificacion.CARGAR_VALORES, trabajo.toNotificacion());
                notificacionEvent.fire(notificacion2);
            } else if (trabajo_old.getEstado().equals(Estado.EN_PROCESO.name()) &&
                    trabajo.getEstado().equals(Estado.PENDIENTE_ASIGNACION_VALORES.name())) {

                Notificacion notificacion = new Notificacion(TipoNotificacion.CARGAR_VALORES, trabajo.toNotificacion());
                notificacionEvent.fire(notificacion);
            } else if (trabajo_old.getEstado().equals(Estado.PENDIENTE_ASIGNACION_VALORES.name()) &&
                    trabajo.getEstado().equals(Estado.PENDIENTE_FACTURA)) {

                Notificacion notificacion = new Notificacion(TipoNotificacion.FACTURA_ERP, trabajo.toNotificacion());
                notificacionEvent.fire(notificacion);
            }


            trabajo.setId(id);
            trabajo = trabajoDao.save(trabajo);


            return Response.ok(trabajo).build();
        } catch (Exception e) {
            return Response.notModified().entity(e.getMessage()).build();
        }
    }


    @DELETE
    @Logged
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Delete Trabajo", response = Trabajo.class)
    public Response delete(@PathParam("id") Long id) {
        try {
            Trabajo trabajo = trabajoDao.findById(id);
            if (trabajo == null) throw new MagnesiumBdNotFoundException("Trabajo no encontrado");

            trabajo.setEstado(Estado.BORRADO.name());
            trabajoDao.save(trabajo);

            return Response.ok(trabajo).build();
        } catch (MagnesiumBdNotFoundException e) {
            logger.warning(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("countField/{field}/{value}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get Count Trabajos", response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Estado no encontrado")})
    public Response countByField(@PathParam("field") String field, @PathParam("value") String value) {
        logger.severe(field);
        logger.severe(value);
        String[] valores = value.split(",");
        Long count = 0L;
        if(field.equals("esReparacion")) {
            for (String valor : valores) {
                if(valor.equals("true"))
                    count += trabajoDao.countByField(field, true);
                else
                    count += trabajoDao.countByField(field, false);
            }
        }
        else{
            for (String valor : valores) {
                count += trabajoDao.countByField(field, valor);
            }
        }


        return Response.ok(count).build();
    }


    @GET
    @Path("count")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get Count Trabajos", response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Estado no encontrado")})
    public Response countAll() {

        Long count = trabajoDao.countAll();
        return Response.ok(count).build();
    }



}
