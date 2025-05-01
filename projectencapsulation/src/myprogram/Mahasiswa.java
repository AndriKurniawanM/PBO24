package myprogram;

public class Mahasiswa extends Manusia{
    private String nim;
    public Mahasiswa(String nama, String alamat, String nim) {
        super.setNama(nama);
        super.setAlamat(alamat);
        this.nim = nim;
    }
    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }
    public String getNama() {
        return super.getNama();
    }

    public void setNama(String nama) {
        super.setNama(nama);
    }

    public String getAlamat() {
        return super.getAlamat();
    }

    public void setAlamat(String alamat) {
        super.setAlamat(alamat);
    }

    @Override
    public String toString() {
        return "Nama\t: " + getNama() +
               "\nNIM\t\t: " + getNim() +
               "\nAlamat\t: " + getAlamat();
    }
}
