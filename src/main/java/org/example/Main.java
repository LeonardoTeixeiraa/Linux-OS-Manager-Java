package org.example;

import org.example.service.LimpezaService;
import org.example.service.SistemaService;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int op;

        do {
            System.out.println("=======CLI OTIMIZADOR DE SISTEMA=======");
            System.out.println("1 - Limpar arquivos temporários");
            System.out.println("2 - Mostrar desempenho da máquina");
            System.out.println("0 - Sair");
            System.out.println("Opção: ");
            op = sc.nextInt();

            switch (op) {
                case 1:
                    System.out.println("Tem certeza que deseja excluir os arquivos?(y/n)");
                    LimpezaService.limparArquivosTemp();
                    break;

                case 2:
                    SistemaService.mostrarDesempenho();
                    break;
                case 0:
                    System.out.println("Saindo");
                    for (int i = 0; i < 3; i++) {
                        System.out.println(".");
                    }
                default:
                    System.out.println("Opção inválida!");
            }

        } while (op != 0);
    }
}