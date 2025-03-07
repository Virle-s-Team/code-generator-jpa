package cool.auv.codegeneratorjpa.core.entity;

import cool.auv.codegeneratorjpa.core.annotation.AutoEntity;
import lombok.NonNull;

public record EntityAnnotations(
        @NonNull AutoEntity autoEntity
){}
