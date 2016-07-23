package crudhibernate.interfaces;

import java.util.List;

/**
 *
 * @author <h1>Joan Im</h1>
 * @param <Entity> <span>Entity to be persisted</span>
 */
public interface CrudMethods<Entity> {

    /**
     *
     * @param entity <span>Entity to be persisted.</span>
     * @return <span>true or false to indicate if the POJO was persisted.</span>
     */
    public boolean insert(Entity entity);

    /**
     *
     * @param entity <span>Entity to be updated.</span>
     * @return <span>true or false to indicate if the POJO was updated.</span>
     */
    public boolean update(Entity entity);

    /**
     *
     * @param className <span>Entity to be deleted.</span>
     * @param id <span>Id of the entity which is gonna be deleted</span>
     * @return <span>true or false to indicate if the POJO was deleted.</span>
     */
    public boolean delete(String className, int id);

    /**
     *
     * @param className <span>Entity to be retrieved.</span>
     * @param id <span>Id of the entity which is gonna be retrieved</span>
     * @return <span>The retrieved entity.</span>
     */
    public Entity getById(String className, int id);

    /**
     *
     * @param className <span>Entity to be retrivied.</span>
     * @param columnName <span>Name of the column in where the LIKE CLAUSE is
     * gonna be applied.</span>
     * @param valueToFilter <span>The LIKE CLAUSE to be applied: %abc, %abc%,
     * abc%</span>
     * @param pageNumber <span>Page number for the pagination. Page where we're
     * located</span>
     * @param rowPerPage <span>Row per page for the pagination. How many rows to
     * display</span>
     * @return <span>The retrieved entity.</span>
     */
    public List<Entity> getByLike(String className, String columnName, String valueToFilter, int pageNumber, int rowPerPage);

    /**
     *
     * @param className <span>Entity to be retrivied.</span>
     * @return <span>The total of rows.</span>
     */
    public Long getTotalRows(String className);

    /**
     *
     * @param className <span>Entities to be retrivied.</span>
     * @param pageNumber <span>Page number for the pagination. Page where we're
     * located</span>
     * @param rowPerPage <span>Row per page for the pagination. How many rows to
     * display</span>
     * @return <span>The retrieved entities.</span>
     */
    public List<Entity> getEntities(String className, int pageNumber, int rowPerPage);

    /**
     *
     * @param className <span>Entity to be retrivied.</span>
     * @param email <span>Email of the user</span>
     * @param password <span>Password of the user</span>
     * @return <span>The retrieved entity.</span>
     */
    public Entity login(String className, String email, String password);

    /**
     *
     * @param className <span>Entity to be retrivied.</span>
     * @param email <span>Email of the user</span>
     * @return <span>The retrieved entity.</span>
     */
    public Entity forgottenPassword(String className, String email);
}
