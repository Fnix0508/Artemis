package com.fnix.artemis.updater.systemtask;

import com.fnix.artemis.core.dto.SystemTaskType;
import com.fnix.artemis.core.model.SystemTask;

public interface SystemTaskProcessor {

    SystemTaskType getType();

    void process(SystemTask systemTask);
}
