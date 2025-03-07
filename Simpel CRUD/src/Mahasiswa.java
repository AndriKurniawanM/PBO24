public class Mahasiswa {
    private String nim;
    private String nama;
    private String alamat;
    private int semester;
    private int sks;
    private double ipk;

    public Mahasiswa(String nim, String nama, String alamat, int semester, int sks, double ipk) {
        this.nim = nim;
        this.nama = nama;
        this.alamat = alamat;
        this.semester = semester;
        this.sks = sks;
        this.ipk = ipk;
    }

    public String getNim() {
        return nim;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public int getSemester() {
        return semester;
    }

    public int getSks() {
        return sks;
    }

    public double getIpk() {
        return ipk;
    }

    @Override
    public String toString() {
        return "MAHASISWA BERIKUT: \n" +
                "NIM      : " + nim.toUpperCase() + "\n"+
                "NAMA     : " + nama.toUpperCase() + "\n" +
                "ALAMAT   : " + alamat.toUpperCase() + "\n" +
                "SEMESTER : " + semester + "\n" +
                "SKS      : " + sks + "\n" +
                "IPK      : " + ipk + "\n";
    }
}