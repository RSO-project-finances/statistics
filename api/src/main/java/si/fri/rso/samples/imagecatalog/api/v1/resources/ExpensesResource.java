package si.fri.rso.samples.imagecatalog.api.v1.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.fri.rso.samples.imagecatalog.lib.Expenses;
import si.fri.rso.samples.imagecatalog.lib.Stat;
import si.fri.rso.samples.imagecatalog.services.beans.ExpensesBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;



@ApplicationScoped
@Path("/statistics")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExpensesResource {

    private Logger log = Logger.getLogger(ExpensesResource.class.getName());

    @Inject
    private ExpensesBean expensesBean;


    @Context
    protected UriInfo uriInfo;

    @Operation(description = "Get all expenses.", summary = "")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "List of expenses",
                    content = @Content(schema = @Schema(implementation = Expenses.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            )})
    @GET
    public Response getExpenses() {

        List<Expenses> expenses = expensesBean.getExpensesFilter(uriInfo);
        for (Expenses expense : expenses) {
            System.out.println(expense);
        }

        List<Stat> stats = returnStats(expenses);

        return Response.status(Response.Status.OK).entity(stats).build();
    }

    public List<Stat> returnStats(List<Expenses> expenses) {
        double average = expenses.stream().mapToDouble(Expenses::getPrice).average().orElse(0.0);
        double sum = expenses.stream().mapToDouble(Expenses::getPrice).sum();
        double min = expenses.stream().mapToDouble(Expenses::getPrice).min().orElse(0.0);
        double max = expenses.stream().mapToDouble(Expenses::getPrice).max().orElse(0.0);

        List<Stat> stats = new ArrayList<>();
        stats.add(new Stat("average", average, null, null));
        stats.add(new Stat("sum", sum, null, null));
        stats.add(new Stat("min", min, null, null));
        stats.add(new Stat("max", max, null, null));

        return stats;
    }
}
