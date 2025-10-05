package org.example.service;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class LimpezaService {

    private static final List<String> DIRECTORY_IGNORE = Arrays.asList(
            "systemd-private-",   // usado por serviços do Linux
            "snap.",              // diretórios temporários de pacotes snap
            "pulse-",             // usado pelo sistema de áudio
            "gvfs",               // sistema de arquivos virtual do GNOME
            "ssh-",               // usado por conexões seguras
            "tmp-",               // pode ser usado por apps em execução
            "runtime-"            // usado pelo sistema em algumas distros
    );

    /* Deletar arquivos de forma recursiva nos diretórios
     */
    public static void limparDiretorio(File file) {
        if (!file.isDirectory()) return;

        File[] listFiles = file.listFiles();

        if (listFiles == null) return;

        for (File arquivo : listFiles) {
            String nome = arquivo.getName();

            boolean ignorar = DIRECTORY_IGNORE.stream().anyMatch(nome::startsWith);

            if (ignorar) {
                continue;
            }

            if (file.isDirectory()) {
                limparDiretorio(arquivo);
            } else {
                arquivo.delete();
            }
        }
        if (!file.getAbsolutePath().equals("/tmp")) {
            file.delete();
        }

    }

    public static void limparArquivosTemp(String confimacao) {
        if (!confimacao.equalsIgnoreCase("S")) return;

        File tmpDirectory = new File("/tmp");

        if (tmpDirectory.exists() && tmpDirectory.isDirectory()) {
            limparDiretorio(tmpDirectory);
            System.out.println("limpeza de arquivos temporários concluída");
        }
    }
}
