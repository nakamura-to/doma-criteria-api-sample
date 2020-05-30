package sample.entity;

import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;

/** */
public class EmployeeListener implements EntityListener<Employee> {

  @Override
  public void preInsert(Employee entity, PreInsertContext<Employee> context) {}

  @Override
  public void preUpdate(Employee entity, PreUpdateContext<Employee> context) {}

  @Override
  public void preDelete(Employee entity, PreDeleteContext<Employee> context) {}

  @Override
  public void postInsert(Employee entity, PostInsertContext<Employee> context) {}

  @Override
  public void postUpdate(Employee entity, PostUpdateContext<Employee> context) {}

  @Override
  public void postDelete(Employee entity, PostDeleteContext<Employee> context) {}
}
