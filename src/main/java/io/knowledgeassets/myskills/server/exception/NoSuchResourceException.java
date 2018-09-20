package io.knowledgeassets.myskills.server.exception;

import io.knowledgeassets.myskills.server.exception.enums.Model;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * If a resource (like entity) does not found, we throw this exception.
 */
public class NoSuchResourceException extends BusinessException {

    @Builder
    private NoSuchResourceException(Model model,String... searchParamsMap) {
        super(NoSuchResourceException.generateMessage(model.toValue(), toMap(String.class, String.class, searchParamsMap)));
    }

    private static String generateMessage(String entity, Map<String, String> searchParams) {
        return StringUtils.capitalize(entity) +
                " was not found for parameters " +
                searchParams;
    }

    /**
     * we search for a resource with keys and values. so a business couldn't find a resource with these keys and values,
     * we have to throw a NoSuchResourceException and we have to mention that for which keys and values, we couldn't find it.
     * so the number of entries always should be even.
     *
     * @param keyType
     * @param valueType
     * @param entries
     * @param <K>
     * @param <V>
     * @return
     */
    private static <K, V> Map<K, V> toMap(
			Class<K> keyType, Class<V> valueType, String... entries) {
        if (entries.length % 2 == 1)
            throw new IllegalArgumentException("Invalid entries");
        return IntStream.range(0, entries.length / 2).map(i -> i * 2)
                .collect(HashMap::new,
                        (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])),
                        Map::putAll);
    }

}
