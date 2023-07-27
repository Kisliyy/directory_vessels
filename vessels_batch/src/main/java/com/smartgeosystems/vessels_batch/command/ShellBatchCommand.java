package com.smartgeosystems.vessels_batch.command;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@RequiredArgsConstructor
@ShellComponent
public class ShellBatchCommand implements BatchCommand {

    private final JobLauncher jobLauncher;
    private final Job importCsvToDb;

    @SneakyThrows
    @Override
    @ShellMethod(key = "start", value = "Start migration data from csv")
    public void startBatch(@ShellOption(help = "path to csv file") String pathToFile) {
        var jobParameters = new JobParametersBuilder()
                .addString("fullPathFileName", pathToFile)
                .toJobParameters();
        jobLauncher.run(importCsvToDb, jobParameters);
    }
}
