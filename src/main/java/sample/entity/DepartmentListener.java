package sample.entity;

import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;

/** */
public class DepartmentListener implements EntityListener<Department> {

  @Override
  public void preInsert(Department entity, PreInsertContext<Department> context) {}

  @Override
  public void preUpdate(Department entity, PreUpdateContext<Department> context) {}

  @Override
  public void preDelete(Department entity, PreDeleteContext<Department> context) {}

  @Override
  public void postInsert(Department entity, PostInsertContext<Department> context) {}

  @Override
  public void postUpdate(Department entity, PostUpdateContext<Department> context) {}

  @Override
  public void postDelete(Department entity, PostDeleteContext<Department> context) {}
}
