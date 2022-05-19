package com.servicec.payment.service;

import com.servicec.payment.entity.Task;

/**
 * @author M.Bezmen
 */
public interface TaskExecutor {

    void execute(Task task);
}
