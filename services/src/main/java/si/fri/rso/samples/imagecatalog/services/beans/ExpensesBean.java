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
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


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

    public List<Expenses> getExpensesFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, ExpensesEntity.class, queryParameters).stream()
                .map(ExpensesConverter::toDto).collect(Collectors.toList());
    }

    public Expenses getExpenses(Integer id) {

        ExpensesEntity ExpensesEntity = em.find(ExpensesEntity.class, id);

        if (ExpensesEntity == null) {
            throw new NotFoundException();
        }

        Expenses expenses = ExpensesConverter.toDto(ExpensesEntity);

        return expenses;
    }

    public Expenses createExpense(Expenses expenses) {

        ExpensesEntity ExpensesEntity = ExpensesConverter.toEntity(expenses);

        try {
            beginTx();
            em.persist(ExpensesEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        if (ExpensesEntity.getExpenseId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return ExpensesConverter.toDto(ExpensesEntity);
    }

    public Expenses putExpense(Integer id, Expenses expenses) {

        ExpensesEntity c = em.find(ExpensesEntity.class, id);

        if (c == null) {
            return null;
        }

        ExpensesEntity updatedExpensesEntity = ExpensesConverter.toEntity(expenses);

        try {
            beginTx();
            updatedExpensesEntity.setExpenseId(c.getExpenseId());
            updatedExpensesEntity = em.merge(updatedExpensesEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        return ExpensesConverter.toDto(updatedExpensesEntity);
    }

    public boolean deleteExpense(Integer id) {

        ExpensesEntity expense = em.find(ExpensesEntity.class, id);

        if (expense != null) {
            try {
                beginTx();
                em.remove(expense);
                commitTx();
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {
            return false;
        }

        return true;
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
