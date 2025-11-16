package dao;

import java.util.List;

public interface GenericDao<T> {
    void insertar(T entidad)throws Exception;
    void actualizar(T entidad)throws Exception;
    void eliminar(int id)throws Exception;
    void recuperar(int id)throws Exception;
    T getById(int id)throws Exception;
    T getByIdEliminado(int id)throws Exception;
    List<T> getAll()throws Exception;
}
