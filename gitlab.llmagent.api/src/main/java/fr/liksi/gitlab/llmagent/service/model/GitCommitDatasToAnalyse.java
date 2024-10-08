package fr.liksi.gitlab.llmagent.service.model;

import org.eclipse.jgit.revwalk.RevCommit;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

public record GitCommitDatasToAnalyse(File targetedRepoDirFile, Map<RevCommit, String> commonCommitDiffs) {

    public List<RevCommit> getCommonCommitsOrderByAscendingDate(){
        return commonCommitDiffs.keySet().stream().sorted(Comparator.comparing(RevCommit::getCommitTime)).toList();
    }

    public List<File> getAllFilesOnTargetedRepo(){
        return Arrays.stream(Objects.requireNonNullElse(targetedRepoDirFile.listFiles(fileFilter()), new File[0]))
                .map(this::getAllNonDirectoryFiles)
                .flatMap(List::stream)
                .toList();
    }

    private List<File> getAllNonDirectoryFiles(File file){
        if(file.isDirectory()){
            return Arrays.stream(Objects.requireNonNullElse(file.listFiles(fileFilter()), new File[0]))
                    .map(this::getAllNonDirectoryFiles)
                    .flatMap(List::stream)
                    .toList();
        }else{
            return List.of(file);
        }
    }

    @NotNull
    private static FileFilter fileFilter() {
        return f -> !f.isHidden() && !f.getName().startsWith(".") && !f.getName().startsWith("mvnw");
    }


}
