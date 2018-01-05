package jterminal.optional.menu;

/**
 * Interface used to define how an object is represented as a String for use in a menu
 * @param <E> the type of the object to be represented
 */
public interface LabelFactory<E>{
    String toLabel(E obj);

}
