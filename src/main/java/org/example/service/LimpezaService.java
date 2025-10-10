package org.example.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class LimpezaService {
    private static final Logger logger = Logger.getLogger(LimpezaService.class.getName());

    private static final List<String> DIRECTORY_IGNORE = Arrays.asList(
            "systemd-private-",   // usado por serviços do Linux
            "snap.",              // diretórios temporários de pacotes snap
            "pulse-",             // usado pelo sistema de áudio
            "gvfs",               // sistema de arquivos virtual do GNOME
            "ssh-",               // usado por conexões seguras
            "tmp-",               // genérico, pode ser usado por apps em execução
            "runtime-"            // usado pelo sistema em algumas distros
    );

    /* Deletar arquivos de forma recursiva nos diretórios
     */
    public static List<String> limparDiretorio(File diretorio) {
        List<String>  deletedFiles = new ArrayList<>();

        if (!diretorio.isDirectory()) return deletedFiles;

        File[] listFiles = diretorio.listFiles();

        if (listFiles == null) return deletedFiles;

        for (File arquivo : listFiles) {
            String nome = arquivo.getName();

            boolean ignorar = DIRECTORY_IGNORE.stream().anyMatch(nome::startsWith);

            if (ignorar) {
                logger.info("Arquivo ignorado (essencial): {}" + arquivo.getAbsolutePath());
                continue;
            }

            if (arquivo.isDirectory()) {
                deletedFiles.addAll(limparDiretorio(arquivo));
                if (arquivo.delete()) {
                    deletedFiles.add("[DIR] " + arquivo.getAbsolutePath());
                } else {
                    logger.warning("Falha ao deletar diretório: " + arquivo.getAbsolutePath());
                }
            } else {
                if (arquivo.delete()) {
                    deletedFiles.add(arquivo.getAbsolutePath());
                } else {
                    logger.warning("Falha ao deletar arquivo: " + arquivo.getAbsolutePath());
                }
            }
        }

        // Não deletar o próprio /tmp
        if (!diretorio.getAbsolutePath().equals("/tmp")) {
            if (!diretorio.delete()) {
                logger.fine("Diretório não deletado (pode não estar vazio): " + diretorio.getAbsolutePath());
            }
        }

        return deletedFiles;
    }

    public static void limparArquivosTemp(String confimacao) {
        if (!confimacao.equalsIgnoreCase("S")) return;

        File tmpDirectory = new File("/tmp");

        if (tmpDirectory.exists() && tmpDirectory.isDirectory()) {
            System.out.println("limpeza de arquivos temporários concluída. Arquivos deletados: " + limparDiretorio(tmpDirectory));
        }
    }
}
