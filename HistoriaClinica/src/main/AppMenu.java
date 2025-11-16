package main;

import dao.HistoriaClinicaDao;
import dao.PacienteDao;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import modelo.GrupoSanguineo;
import modelo.HistoriaClinica;
import modelo.Paciente;

public class AppMenu {

    public static final String RESET = "\u001B[0m";
    public static final String CYAN = "\u001B[36m";
    public static final String GREEN  = "\u001B[32m";
    public static final String RED    = "\u001B[31m";
    public static final String PURPLE = "\u001B[35m";
    public static final String YELLOW = "\u001B[33m";

    private final Scanner scanner = new Scanner(System.in);
    private final PacienteDao pacienteService = new PacienteDao();
    private final HistoriaClinicaDao historiaService = new HistoriaClinicaDao();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        new AppMenu().iniciar();
    }

public void iniciar() {
    int opcion;

    do {
        try {
            mostrarMenu();
            opcion = leerOpcion();

            switch (opcion) {
                case 1 -> crearPaciente();
                case 2 -> listarPacientes();
                case 3 -> buscarPorDni();
                case 4 -> actualizarPaciente();
                case 5 -> eliminarPaciente();
                case 6 -> recuperarPaciente();
                case 7 -> listarHistorias();
                case 8 -> actualizarHistoria();
                case 0 -> System.out.println(PURPLE + "👋 Saliendo..." + RESET);
                default -> System.out.println(RED + "⚠️ Opción inválida" + RESET);
            }

        } catch (Exception e) {
            System.out.println(RED + "⚠️ Error: " + e.getMessage() + RESET);
            opcion = -1; // para que no salga del menú
        }

    } while (opcion != 0);
}

    // MÓDULOS
    private void mostrarMenu() {
        System.out.println("\n" + GREEN + "=== Menú Principal ===" + RESET);
        System.out.println(GREEN+"===   Clinica 53   ==="+RESET);
        System.out.println("1. Crear Paciente");
        System.out.println("2. Listar Pacientes");
        System.out.println("3. Buscar por DNI");
        System.out.println("4. Actualizar Paciente");
        System.out.println("5. Eliminar Paciente");
        System.out.println("6. Recuperar Paciente");
        System.out.println("7. Listar Historias Clínicas");
        System.out.println("8. Actualizar Historia Clínica");
        System.out.println("0. Salir");
        System.out.print("Opción: ");
    }

    private int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            return -1;
        }
    }

    // 1. CREAR PACIENTE
    private void crearPaciente() throws Exception{
        
            System.out.println("\n" + CYAN + "--- Alta de Paciente ---" + RESET);

            String nombre = validarNombre("Nombre: ");
            String apellido = validarApellido("Apellido: ");
            String dni = validarDni("DNI: ");
            LocalDate fecha = validarFecha("Fecha nacimiento (dd/MM/yyyy): ");              
            System.out.println("\n" + CYAN + "--- Datos de Historia Clínica ---" + RESET);
            System.out.println("""
                    Grupo sanguíneo (número): \n
                    0 = Desconocido           
                    1 = A+
                    2 = A-
                    3 = B+
                    4 = B-
                    5 = AB+
                    6 = AB-
                    7 = 0+
                    8 = 0-                    
                    """);

            GrupoSanguineo grupo = validarGrupoSanguineo("Código (0-8): ");

            System.out.print("Antecedentes (opcional): ");
            String antecedentes = optional(scanner.nextLine());

            System.out.print("Medicación (opcional): ");
            String medicacion = optional(scanner.nextLine());

            System.out.print("Observaciones (opcional): ");
            String observaciones = optional(scanner.nextLine());

            // Crear objetos
            HistoriaClinica hc = new HistoriaClinica(
                dni,                     // nroHistoria = dni
                grupo,                   // puede ser null
                antecedentes,
                medicacion,
                observaciones,
                0,
                false
            );

            Paciente paciente = new Paciente(
                nombre,
                apellido,
                dni,
                fecha,
                hc,
                0,
                false
            );

            historiaService.insertar(hc);
            pacienteService.insertar(paciente);

            System.out.println(GREEN + "✔ Paciente creado exitosamente." + RESET);
        
    }

    // 2. LISTAR PACIENTES
    private void listarPacientes() throws Exception {
        System.out.println("\n--- Lista de Pacientes ---");
        List<Paciente> pacientes = pacienteService.getAll();
        pacientes.forEach(System.out::println);
    }

    // 3. BUSCAR POR DNI
    private void buscarPorDni() throws Exception {       
        String dni = validarDni("\nIngrese DNI a buscar: ");      
        Paciente p = pacienteService.getByDni(dni);

        if (p != null) {
            System.out.println(p);
            System.out.println(p.getHistoriaClinica());
        } else {
            System.out.println(RED + "⚠️ Paciente no encontrado." + RESET);
        }
    }

    // 4. ACTUALIZAR PACIENTE
    private void actualizarPaciente() throws Exception {       
        String dni = validarDni("\nIngrese DNI del paciente a actualizar: ");   
        Paciente p = pacienteService.getByDni(dni);
            
        if (p != null) {
            System.out.print("Nuevo nombre (actual: " + p.getNombre() + ", "+CYAN+"Enter para mantener"+RESET+"): ");
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) p.setNombre(input);
            System.out.print("Nuevo apellido (actual: " + p.getApellido() + ", "+CYAN+"Enter para mantener"+RESET+"): ");
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) p.setApellido(input);
            pacienteService.actualizar(p);
            System.out.println(CYAN+"✅ Paciente actualizado."+RESET);
        } else {
            System.out.println(RED+"⚠ Paciente no encontrado."+RESET);
        }
    }

    // 5. ELIMINAR PACIENTE
    private void eliminarPaciente() throws Exception {

        int id = validarId("Ingrese ID del paciente a eliminar: "); 
        Paciente p = pacienteService.getById(id);  

        if (p == null) {
            System.out.println(RED + "⚠ Paciente no encontrado." + RESET);
            return;
        }

        System.out.println("\nPaciente encontrado:");
        System.out.println(p);

        System.out.print("\n¿Confirma la eliminación lógica? (S/N): ");
        String confirma = scanner.nextLine().trim().toUpperCase();

        if (!confirma.equals("S")) {
            System.out.println(YELLOW + "↺ Operación cancelada." + RESET);
            return;
        }

        pacienteService.eliminar(id);  
        System.out.println(GREEN + "✔ Paciente eliminado." + RESET);
    }

    // 6. RECUPERAR PACIENTE
    private void recuperarPaciente() throws Exception {

        int id = validarId("Ingrese ID del paciente a recuperar: "); 
        Paciente p = pacienteService.getByIdEliminado(id);  

        if (p == null) {
            System.out.println(RED + "⚠ Paciente no encontrado." + RESET);
            return;
        }

        System.out.println("\nPaciente encontrado:");
        System.out.println(p);

        System.out.print("\n¿Confirma la recuperación del paciente? (S/N): ");
        String confirma = scanner.nextLine().trim().toUpperCase();

        if (!confirma.equals("S")) {
            System.out.println(YELLOW + "↺ Operación cancelada." + RESET);
            return;
        }

        pacienteService.recuperar(id);
        System.out.println(GREEN + "✔ Paciente recuperado." + RESET);
    }

    // 7. LISTAR HISTORIAS CLÍNICAS
    private void listarHistorias()throws Exception {
        List<HistoriaClinica> historias = historiaService.getAll();
        historias.forEach(System.out::println);
    }

    // 8. ACTUALIZAR HISTORIA CLÍNICA
    private void actualizarHistoria() throws Exception {
        System.out.print("Ingrese NroHistoria a actualizar: ");
        String NroHistoria = scanner.nextLine();

        HistoriaClinica hc = historiaService.getByNroHistoria(NroHistoria);

        if (hc == null) {
            System.out.println(RED + "⚠ Historia no encontrada." + RESET);
            return;
        }

        System.out.print("Nuevo antecedente: ");
        hc.setAntecedentes(scanner.nextLine());

        System.out.print("Nueva medicación: ");
        hc.setMedicacionActual(scanner.nextLine());

        System.out.print("Nueva observación: ");
        hc.setObservaciones(scanner.nextLine());

        historiaService.actualizar(hc);
        System.out.println(GREEN + "✔ Historia actualizada." + RESET);
    }

    // VALIDACIONES
    private String validarNombre(String msg) {
        String valor;
        while (true) {
            System.out.print(msg);
            valor = scanner.nextLine().trim();

            if (valor.isEmpty()) {
                System.out.println(RED + "❌ No puede estar vacío." + RESET);
                continue;
            }
            if (!valor.matches("^[a-zA-ZÁÉÍÓÚÑáéíóúñ ]+$")) {
                System.out.println(RED + "❌ Solo letras." + RESET);
                continue;
            }
            return valor;
        }
    }

    private String validarApellido(String msg) {
        return validarNombre(msg);
    }

    private String validarDni(String msg) {
        String dni;
        while (true) {
            System.out.print(msg);
            dni = scanner.nextLine().trim();

            if (!dni.matches("^[0-9]+$")) {
                System.out.println(RED + "❌ DNI inválido, solo números." + RESET);
                continue;
            }
            return dni;
        }
    }

    private LocalDate validarFecha(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return LocalDate.parse(scanner.nextLine().trim(), fmt);
            } catch (Exception e) {
                System.out.println(RED + "❌ Fecha inválida (dd/MM/yyyy)." + RESET);
            }
        }
    }

    private GrupoSanguineo validarGrupoSanguineo(String msg) {
        while (true) {
            System.out.print(msg);
            String input = scanner.nextLine().trim();

            // validar número del 0 al 8
            if (!input.matches("^[0-8]$")) {
                System.out.println(RED + "❌ Debe ingresar un número entre 0 y 8." + RESET);
                continue;
            }

            int codigo = Integer.parseInt(input);
            return GrupoSanguineo.fromCodigo(codigo);
        }
    }

    private int validarId(String msg) {
        while (true) {
            System.out.print(msg);
            String input = scanner.nextLine().trim();

            if (!input.matches("^[0-9]+$")) {
                System.out.println(RED + "❌ Id inválido, solo números." + RESET);
                continue;
            }

            return Integer.parseInt(input);  // ✔ devuelve int
        }
    }

    private String optional(String s) {
        return s.trim().isEmpty() ? null : s;
    }
}
