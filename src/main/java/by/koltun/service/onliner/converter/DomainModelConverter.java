package by.koltun.service.onliner.converter;

@FunctionalInterface
public interface DomainModelConverter<T, R> {

    /**
     * Converter method
     * @param t input argument
     * @return result of conversion
     */
    R convert(T t);
}
