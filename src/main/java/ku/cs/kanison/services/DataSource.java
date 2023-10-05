package ku.cs.kanison.services;

public interface DataSource <T> {
    void readData();
    T getData();
    void writeData(T t);
}
