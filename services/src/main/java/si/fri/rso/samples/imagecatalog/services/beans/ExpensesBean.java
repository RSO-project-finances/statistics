package si.fri.rso.samples.imagecatalog.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.samples.imagecatalog.lib.Expenses;
import si.fri.rso.samples.imagecatalog.models.converters.ExpensesConverter;
import si.fri.rso.samples.imagecatalog.models.entities.ExpensesEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;


@RequestScoped
public class ExpensesBean {

    private Logger log = Logger.getLogger(ExpensesBean.class.getName());

    @Inject
    private EntityManager em;

    public List<Expenses> getExpenses() {

        TypedQuery<ExpensesEntity> query = em.createNamedQuery(
                "ExpensesEntity.getAll", ExpensesEntity.class);

        List<ExpensesEntity> resultList = query.getResultList();

        return resultList.stream().map(ExpensesConverter::toDto).collect(Collectors.toList());

    }

    @Timeout(value = 5, unit = ChronoUnit.SECONDS)
    @CircuitBreaker
    @Fallback(fallbackMethod = "getExpensesFallback")
    public List<Expenses> getExpensesFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, ExpensesEntity.class, queryParameters).stream()
                .map(ExpensesConverter::toDto).collect(Collectors.toList());
    }

    public List<Expenses> getExpensesFallback(UriInfo uriInfo) {
        return null;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
