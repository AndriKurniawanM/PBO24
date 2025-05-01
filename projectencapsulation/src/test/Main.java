package test;

import myprogram.Mahasiswa;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Mahasiswa> isi = new ArrayList<>();
        isi.add(new Mahasiswa("Aika" , "Fontaine" , "D02242171"));
        isi.add(new Mahasiswa("Furina" , "Fontaine" , "D02242172"));
        isi.add(new Mahasiswa("Klee" , "Mondstat" , "D02242173"));
        isi.add(new Mahasiswa("Raiden Ei" , "Inazuma" , "D02242174"));
        isi.add(new Mahasiswa("Venti" , "Mondstat" , "D02242175"));
        isi.add(new Mahasiswa("Wrothesley" , "Fontaine" , "D02242176"));
        isi.add(new Mahasiswa("Clara" , "Belebog" , "D02242177"));
        isi.add(new Mahasiswa("Ningguang" , "Liyue" , "D02242178"));
        isi.add(new Mahasiswa("Keqing" , "Liyue" , "D02242179"));
        isi.add(new Mahasiswa("Hu Tao" , "Liyue" , "D02242180"));
        isi.add(new Mahasiswa("Qiqi" , "Liyue" , "D02242181"));
        isi.add(new Mahasiswa("Xiao" , "Liyue" , "D02242182"));
        isi.add(new Mahasiswa("Ganyu" , "Liyue" , "D02232183"));
        isi.add(new Mahasiswa("Yelan" , "Liyue" , "D02232184"));
        isi.add(new Mahasiswa("Diona" , "Mondstat" , "D02232185"));
        isi.add(new Mahasiswa("Yae Miko" , "Inazuma" , "D02232186"));
        isi.add(new Mahasiswa("Kuki Shinobu" , "Inazuma" , "D02232187"));
        isi.add(new Mahasiswa("Xiangling" , "Liyue" , "D02232188"));
        isi.add(new Mahasiswa("Mona" , "Mondstat" , "D02232189"));
        isi.add(new Mahasiswa("Nahida" , "Sumeru" , "D02232190"));

        for(Mahasiswa m : isi) {
            System.out.println(m);
            System.out.println("------------------------------");
        }
    }
}
