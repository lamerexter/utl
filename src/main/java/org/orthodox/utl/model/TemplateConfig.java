package org.orthodox.utl.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TemplateConfig {
    private boolean interpolate;
}
