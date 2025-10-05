package org.example.service;

import org.slf4j.LoggerFactory;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;

public class MonitoramentoSistemaService {
    private static final Logger logger = LoggerFactory.getLogger(MonitoramentoSistemaService.class);
    private final SystemInfo systemInfo;
    private final HardwareAbstractionLayer hardware;
    private final CentralProcessor processor;
    private final GlobalMemory memory;

    public MonitoramentoSistemaService() {
        this.systemInfo = new SystemInfo();
        this.hardware = systemInfo.getHardware();
        this.processor = hardware.getProcessor();
        this.memory = hardware.getMemory();
    }

    public void checkSystemUsage(String serviceName) {
        logInfo("Monitoramento do sistema \n", serviceName);
        try {
            checkCpuUsage(serviceName);
            checkMemoryUsage(serviceName);
            checkDiskUsage(serviceName);
            checkNetworkUsage(serviceName);
        } catch (Exception e) {
            logError("Erro no monitoramento do sistema", serviceName, e);
        }
        logInfo(" Monitoramento de sistema completo\n", serviceName);
    }

    private void checkCpuUsage(String serviceName) throws InterruptedException {
        long[] oldTicks = processor.getSystemCpuLoadTicks();
        Thread.sleep(1000);
        double cpuLoad = processor.getSystemCpuLoadBetweenTicks(oldTicks) * 100;
        logInfo(String.format("Uso de CPU: %.2f%%", cpuLoad), serviceName);
    }

    private void checkDiskUsage(String serviceName) {
        File root = new File("/");
        long totalSpace = root.getTotalSpace();
        long freeSpace = root.getFreeSpace();
        logInfo(String.format("Uso de disco: %dGB livre de %dGB",
                freeSpace / 1073741824, totalSpace / 1073741824), serviceName);
    }

    private void checkMemoryUsage(String serviceName) {
        long totalMemory = memory.getTotal();
        long avaliableMemory = memory.getAvailable();
        long usedMemory = totalMemory - avaliableMemory;
        double usagePercentage = (double) usedMemory / totalMemory * 100;
        logInfo(String.format("Uso de memória: %.2f%% (Usados: %dMB / Total: %dMB)",
                usagePercentage, usedMemory / 1048576, totalMemory / 1048576), serviceName);
    }

    private void checkNetworkUsage(String serviceName) {
        List<NetworkIF> networkInterfaces = hardware.getNetworkIFs();
        for (NetworkIF net : networkInterfaces) {
            logInfo(String.format("Interface de Rede: %s - Recebidos: %dMB, Enviados: %dMB ",
                    net.getName(), net.getBytesRecv() / 1048576, net.getBytesSent() / 1048576), serviceName);
        }
    }


    private void logInfo(String message, String serviceName) {
        logger.info("[{}] {}", serviceName, message);
    }

    private void logError(String message, String serviceName, Exception e) {
        logger.error("[{}] {} - {}", serviceName, message, e.getMessage());
    }

}
