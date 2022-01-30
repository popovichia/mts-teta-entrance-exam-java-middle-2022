package com.example.demo.core.factories;

import com.example.demo.core.data.CommandLine;
import com.example.demo.core.data.Response;
import com.example.demo.core.data.Task;
import com.example.demo.core.enums.Result;
import com.example.demo.core.enums.Status;
import com.example.demo.core.exceptions.AccessDeniedException;
import com.example.demo.core.exceptions.ErrorException;

public class ReopenTaskAction extends Action {

  @Override
  public Response execute(CommandLine commandLine) {

    if (!validator.validate(commandLine)) {
      throw new ErrorException(Result.WRONG_FORMAT.toString());
    }

    Task task = tasksRepository.findByName(commandLine.getArg());
    if (task != null && !task.getOwner().equals(commandLine.getUser())) {
      throw new AccessDeniedException(Result.ACCESS_DENIED.toString());
    }

    if (task == null || task.getStatus() != Status.CLOSED) {
      throw new ErrorException(Result.ERROR.toString());
    }

    task.setStatus(Status.CREATED);
    return new Response(Result.REOPENED);
  }
}