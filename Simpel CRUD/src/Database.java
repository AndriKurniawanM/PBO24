import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private ArrayList<Mahasiswa> data = new ArrayList<>();
    private String filename = "src/data.csv";
    private Path path = Path.of(filename);

    public Database() {
        open();
    }

    public ArrayList<Mahasiswa> getData() {
        return data;
    }
    public void open() {
        try {
            List<String> lines = Files.readAllLines(path);
            data = new ArrayList<>();
            for(int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] element = line.split(",");
                String nim = element[0];
                String nama = element[1];
                String alamat = element[2];
                int semester = Integer.parseInt(element[3]);
                int sks = Integer.parseInt(element[4]);
                double ipk = Double.parseDouble(element[5]);
                Mahasiswa mhs = new Mahasiswa(nim, nama, alamat, semester, sks, ipk);
                data.add(mhs);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        StringBuilder sb = new StringBuilder();
        sb.append("NIM, NAMA, ALAMAT, SEMESTER, SKS, IPK\n");
        if(!data.isEmpty()) {
            for(int i = 0; i < data.size(); i++) {
                Mahasiswa mhs = data.get(i);
                String line = mhs.getNim() + "," + mhs.getNama() + "," + mhs.getAlamat() + "," + mhs.getSemester() + "," + mhs.getSks() + "," + mhs.getIpk() + "\n";
                sb.append(line);
            }
        }
        try {
            Files.writeString(path,sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void view() {
        System.out.println("=============================================================================");
        System.out.printf("| %-8.8S |", "NIM");
        System.out.printf(" %-20.20S |", "NAMA");
        System.out.printf(" %-15.15S |", "ALAMAT(KOTA)");
        System.out.printf(" %8.8S |", "SEMESTER");
        System.out.printf(" %3.3S |", "SKS");
        System.out.printf(" %4.4S |%n", "IPK");
        System.out.println("-----------------------------------------------------------------------------");
        for(Mahasiswa mhs : data) {
            System.out.printf("| %-8.8S |", mhs.getNim());
            System.out.printf(" %-20.20S |", mhs.getNama());
            System.out.printf(" %-15.15S |", mhs.getAlamat());
            System.out.printf(" %8.8S |", mhs.getSemester());
            System.out.printf(" %3.3S |", mhs.getSks());
            System.out.printf(" %4.4S |", mhs.getIpk());
            System.out.println();
        }
        System.out.println("-----------------------------------------------------------------------------");
    }

    public boolean insert(String nim, String nama, String alamat, int semester, int sks, double ipk) {
        boolean status = true;
        //cek primary key
        if(!data.isEmpty()) {
            for(int i = 0; i < data.size(); i++) {
                if(data.get(i).getNim().equalsIgnoreCase(nim)) {
                    status = false;
                    break;
                }
            }
        }
        if(status == true) {
            Mahasiswa mhs = new Mahasiswa(nim, nama, alamat, semester, sks, ipk);
            data.add(mhs);
            save();
        }
        return status;
    }

    public int search(String nim) {
        int index = -1;
        if(!data.isEmpty()) {
            for(int i = 0; i < data.size(); i++) {
                if(data.get(i).getNim().equalsIgnoreCase(nim)) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    public boolean update(int index, String nim, String nama, String alamat, int semester, int sks, double ipk) {
        boolean status = false;
        //update
        if(!data.isEmpty()) {
            if(index >= 0 && index < data.size()) {
                Mahasiswa mhs = new Mahasiswa(nim, nama, alamat, semester, sks, ipk);
                data.set(index, mhs);
                save();
                status = true;
            }
        }
        return status;
    }

    public boolean delete(int index) {
        boolean status = false;
        if(!data.isEmpty()) {
            data.remove(index);
            save();
            status = true;
        }
        return status;
    }

}