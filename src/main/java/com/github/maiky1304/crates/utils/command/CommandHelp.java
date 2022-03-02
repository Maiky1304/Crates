package com.github.maiky1304.crates.utils.command;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class CommandHelp {

    private final Command command;

    private final List<SubCommandInfo> subCommandInfoList = new ArrayList<>();

    private int perPage;

    public CommandHelp(Command command) {
        this.command = command;
        this.perPage = 5;
        this.fetchMethods();
    }

    public CommandHelp(Command command, int perPage) {
        this(command);
        this.perPage = perPage;
    }

    public int getPages() {
        return (int) Math.ceil((double) subCommandInfoList.size() / perPage);
    }

    public List<SubCommandInfo> getRange(int page) {
        if (page > getPages()) {
            return Collections.emptyList();
        }

        return subCommandInfoList.stream().skip((long) page * perPage)
                .limit(perPage).collect(Collectors.toList());
    }

    private void fetchMethods() {
        Arrays.stream(this.command.getClass()
                .getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(SubCommandInfo.class))
                .map(method -> method.getDeclaredAnnotation(SubCommandInfo.class))
                .forEach(subCommandInfoList::add);
    }

}
