package uz.pdp.maven.backend.service.bookService.filter;

public interface Filter<O> {

    boolean check(O object);
}
