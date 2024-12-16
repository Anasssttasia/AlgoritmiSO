package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

@Controller
public class ProcessSchedulerController {

    // Clasa pentru procese
    public static class Process {
        int id;
        int burstTime;   // Timpul de execuție
        int waitingTime; // Timpul de așteptare
        int turnaroundTime; // Timpul de întoarcere

        public Process(int id, int burstTime) {
            this.id = id;
            this.burstTime = burstTime;
        }
    }

    // Algoritmul FCFS
    public static String fcfs(List<Process> processes) {
        int totalWaitingTime = 0, totalTurnaroundTime = 0;
        processes.get(0).waitingTime = 0; // primul proces nu are timp de așteptare

        for (int i = 1; i < processes.size(); i++) {
            processes.get(i).waitingTime = processes.get(i - 1).waitingTime + processes.get(i - 1).burstTime;
        }

        for (Process process : processes) {
            process.turnaroundTime = process.burstTime + process.waitingTime;
            totalWaitingTime += process.waitingTime;
            totalTurnaroundTime += process.turnaroundTime;
        }

        StringBuilder result = new StringBuilder("Rezultate pentru FCFS:\n");
        for (Process process : processes) {
            result.append("ID Proces: ").append(process.id)
                  .append(", Timp execuție: ").append(process.burstTime)
                  .append(", Timp așteptare: ").append(process.waitingTime)
                  .append(", Timp de întoarcere: ").append(process.turnaroundTime)
                  .append("\n");
        }
        result.append("\nTimpul mediu de așteptare: ").append((float) totalWaitingTime / processes.size())
              .append("\nTimpul mediu de întoarcere: ").append((float) totalTurnaroundTime / processes.size());

        return result.toString();
    }

    // Algoritmul SJF
    public static String sjf(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.burstTime)); // Sortează procesele după burst time

        int totalWaitingTime = 0, totalTurnaroundTime = 0;
        processes.get(0).waitingTime = 0;

        for (int i = 1; i < processes.size(); i++) {
            processes.get(i).waitingTime = processes.get(i - 1).waitingTime + processes.get(i - 1).burstTime;
        }

        for (Process process : processes) {
            process.turnaroundTime = process.burstTime + process.waitingTime;
            totalWaitingTime += process.waitingTime;
            totalTurnaroundTime += process.turnaroundTime;
        }

        StringBuilder result = new StringBuilder("Rezultate pentru SJF:\n");
        for (Process process : processes) {
            result.append("ID Proces: ").append(process.id)
                  .append(", Timp execuție: ").append(process.burstTime)
                  .append(", Timp așteptare: ").append(process.waitingTime)
                  .append(", Timp de întoarcere: ").append(process.turnaroundTime)
                  .append("\n");
        }
        result.append("\nTimpul mediu de așteptare: ").append((float) totalWaitingTime / processes.size())
              .append("\nTimpul mediu de întoarcere: ").append((float) totalTurnaroundTime / processes.size());

        return result.toString();
    }

    // Algoritmul Round Robin
    public static String roundRobin(List<Process> processes, int quantum) {
        Queue<Process> queue = new LinkedList<>(processes);
        int totalWaitingTime = 0, totalTurnaroundTime = 0;
        int currentTime = 0;

        while (!queue.isEmpty()) {
            Process process = queue.poll();
            int remainingBurstTime = process.burstTime;

            if (remainingBurstTime > quantum) {
                process.burstTime -= quantum;
                currentTime += quantum;
                queue.add(process);
            } else {
                currentTime += remainingBurstTime;
                process.burstTime = 0;
                process.waitingTime = currentTime - process.burstTime;
                process.turnaroundTime = currentTime;
            }
        }

        for (Process process : processes) {
            totalWaitingTime += process.waitingTime;
            totalTurnaroundTime += process.turnaroundTime;
        }

        StringBuilder result = new StringBuilder("Rezultate pentru Round Robin:\n");
        for (Process process : processes) {
            result.append("ID Proces: ").append(process.id)
                  .append(", Timp execuție: ").append(process.burstTime)
                  .append(", Timp așteptare: ").append(process.waitingTime)
                  .append(", Timp de întoarcere: ").append(process.turnaroundTime)
                  .append("\n");
        }
        result.append("\nTimpul mediu de așteptare: ").append((float) totalWaitingTime / processes.size())
              .append("\nTimpul mediu de întoarcere: ").append((float) totalTurnaroundTime / processes.size());

        return result.toString();
    }

    @GetMapping("/")
    public String showForm() {
        return "index"; // Afișează fișierul index.html
    }

    @GetMapping("/processScheduler")
public String handleScheduler(
    @RequestParam int n,
    @RequestParam String algorithm,
    @RequestParam(required = false, defaultValue = "0") int quantum,
    @RequestParam List<Integer> burstTimes, // Preia lista de burstTimes de la utilizator
    Model model) {
        
    // Verificăm dacă lista de burstTimes are dimensiunea corectă
    if (burstTimes.size() != n) {
        model.addAttribute("error", "Numărul de burstTimes nu corespunde cu numărul de procese!");
        return "index"; // Dacă nu corespund, întoarcem la formularul de intrare
    }

    // Creăm procesele pe baza valorilor burstTime
    List<Process> processes = new ArrayList<>();
    for (int i = 0; i < n; i++) {
        processes.add(new Process(i + 1, burstTimes.get(i))); // Adăugăm burstTime-ul introdus de utilizator
    }

    String result = "";
    switch (algorithm) {
        case "FCFS":
            result = fcfs(processes);
            break;
        case "SJF":
            result = sjf(processes);
            break;
        case "RoundRobin":
            result = roundRobin(processes, quantum);
            break;
        default:
            result = "Algoritm invalid!";
    }

    // Adăugăm rezultatele în model
    model.addAttribute("result", result);
    return "result"; // Afișează rezultatul în fișierul result.html
}

}
