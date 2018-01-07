package core.character.stats;

import java.io.Serializable;
import java.util.function.Function;

public interface SerializableLink<I, O> extends Function<I,O>, Serializable{

}
