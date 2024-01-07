package si.fri.rso.samples.imagecatalog.models.converters;

import si.fri.rso.samples.imagecatalog.lib.Expenses;
import si.fri.rso.samples.imagecatalog.models.entities.ExpensesEntity;

public class ExpensesConverter {

    public static Expenses toDto(ExpensesEntity entity) {

        Expenses dto = new Expenses();
        dto.setExpenseId(entity.getExpenseId());
        dto.setKind(entity.getKind());
        dto.setDateOccurrence(entity.getDateoccurrence());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());

        return dto;

    }

    public static ExpensesEntity toEntity(Expenses dto) {

        ExpensesEntity entity = new ExpensesEntity();
        entity.setExpenseId(dto.getExpenseId());
        entity.setKind(dto.getKind());
        entity.setDateoccurrence(dto.getDateOccurrence());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());


        return entity;

    }

}
