package com.serdar_kara.bilfit.algorithm;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.*;
import java.util.ArrayList;

public class KullaniciArayuzu extends JFrame{
    
    User bizimUser;

    JPanel tercihlerPaneli;
    
    JPanel cinsiyetPaneli;
    JPanel hedefBolgePaneli;
    JPanel ibmPaneli;
    JPanel vucutTipiPaneli;
    JPanel sinavPaneli;
    JPanel hangiGunPaneli;

    JRadioButton erkek;
    JRadioButton kiz;

    JCheckBox sirt, gogus, kol, bacak;

    JLabel ibmLabel;
    JTextField ibmField;

    JRadioButton siska, normal, obez, cokKasli;

    JLabel sinavLabel;
    JTextField sinavField;

    JCheckBox pzt, sali, cars, pers, cuma, cmrts, pazar;

    JButton programiOlusturButonu;

    JPanel programiGosterenPanel;

    public KullaniciArayuzu(User sporcu)
    {
        tercihlerPaneli = new JPanel(new GridLayout(1, 6));
        cinsiyetPaneliOlustur();
        hedefBolgePaneliOlustur();
        ibmPaneliOlusturucu();
        vucutTipiPaneliOlusturucu();
        sinavPaneliOlusturucu();
        hangiGunPaneliOlusturucu();

        add(tercihlerPaneli, BorderLayout.NORTH);

        programiOlusturButonu = new JButton("Programi Olustur");
        programiOlusturButonu.addActionListener(new butonListener());
        this.add(programiOlusturButonu, BorderLayout.WEST);

        setSize(1200,600);
        bizimUser = sporcu;
        
    }

    private void cinsiyetPaneliOlustur()
    {
        this.cinsiyetPaneli = new JPanel(new GridLayout(1,2));
        erkek = new JRadioButton("erkek");
        kiz = new JRadioButton("kiz");

        ButtonGroup g = new ButtonGroup();
        g.add(erkek);
        g.add(kiz);

        this.cinsiyetPaneli.add(erkek);
        this.cinsiyetPaneli.add(kiz);

        this.tercihlerPaneli.add(cinsiyetPaneli);
    }

    private void hedefBolgePaneliOlustur()
    {
        this.hedefBolgePaneli = new JPanel(new GridLayout(2, 2));
        sirt = new JCheckBox("sirt");
        gogus = new JCheckBox("gogus");
        kol = new JCheckBox("kol");
        bacak = new JCheckBox("bacak");

        this.hedefBolgePaneli.add(sirt);
        this.hedefBolgePaneli.add(kol);
        this.hedefBolgePaneli.add(gogus);
        this.hedefBolgePaneli.add(bacak);
        
        this.tercihlerPaneli.add(hedefBolgePaneli);
    }

    private void ibmPaneliOlusturucu()
    {
        this.ibmPaneli = new JPanel();
        this.ibmLabel = new JLabel("ibm gir:");
        this.ibmField = new JTextField(5);

        this.ibmPaneli.add(ibmLabel);
        this.ibmPaneli.add(ibmField);

        this.tercihlerPaneli.add(ibmPaneli);
    }

    private void vucutTipiPaneliOlusturucu()
    {
        this.vucutTipiPaneli = new JPanel(new GridLayout(4, 1));
     
        siska = new JRadioButton("Siska");
        normal = new JRadioButton("Normal");
        obez = new JRadioButton("Obez");
        cokKasli = new JRadioButton("Çok Kasli");

        ButtonGroup g = new ButtonGroup();
        g.add(siska);
        g.add(normal);
        g.add(obez);
        g.add(cokKasli);

        vucutTipiPaneli.add(siska);
        vucutTipiPaneli.add(normal);
        vucutTipiPaneli.add(obez);
        vucutTipiPaneli.add(cokKasli);

        this.tercihlerPaneli.add(vucutTipiPaneli);
    }

    private void sinavPaneliOlusturucu()
    {
        this.sinavPaneli = new JPanel();
        this.sinavLabel = new JLabel("kac sinav cekion");
        this.sinavField = new JTextField(5);
        
        this.sinavPaneli.add(sinavLabel);
        this.sinavPaneli.add(sinavField);

        this.tercihlerPaneli.add(sinavPaneli);
    }

    private void hangiGunPaneliOlusturucu()
    {
        pzt = new JCheckBox("pzt");
        sali = new JCheckBox("sali");
        cars = new JCheckBox("cars");
        pers = new JCheckBox("pers");
        cuma = new JCheckBox("cuma");
        cmrts = new JCheckBox("cmts");
        pazar = new JCheckBox("pazar");

        this.hangiGunPaneli = new JPanel(new GridLayout(3, 3));
        this.hangiGunPaneli.add(pzt);
        this.hangiGunPaneli.add(sali);
        this.hangiGunPaneli.add(cars);
        this.hangiGunPaneli.add(pers);
        this.hangiGunPaneli.add(cuma);
        this.hangiGunPaneli.add(cmrts);
        this.hangiGunPaneli.add(pazar);

        this.tercihlerPaneli.add(hangiGunPaneli);
    }

    class butonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
   
            double ibm = Integer.parseInt(ibmField.getText());
            if ((5 - Math.abs(24.0 - ibm)) > 0)
            {bizimUser.gucuArttir((5 - Math.abs(24.0 - ibm)) / 5);}

            if (siska.isSelected())
            {
                bizimUser.gucuArttir(0.4);
            }
            else if (normal.isSelected())
            {
                bizimUser.gucuArttir(0.8);
            }
            else if (obez.isSelected())
            {
                bizimUser.gucuArttir(0.2);
            }
            else if (cokKasli.isSelected())
            {
                bizimUser.gucuArttir(1.5);
            }

            double sinavSayisi = Integer.parseInt(sinavField.getText());
            bizimUser.gucuArttir(sinavSayisi / 12);

            int gunSayisi = 0;
            if (pzt.isSelected())
            {
                gunSayisi++;
            }
            if (sali.isSelected())
            {
                gunSayisi++;
            }
            if (cars.isSelected())
            {
                gunSayisi++;
            }
            if (pers.isSelected())
            {
                gunSayisi++;
            }
            if (cuma.isSelected())
            {
                gunSayisi++;
            }
            if (cmrts.isSelected())
            {
                gunSayisi++;
            }
            if (pazar.isSelected())
            {
                gunSayisi++;
            }
            
            bizimUser.setGunSayisi(gunSayisi);

            bizimUser.programiOlustur();

            

            if (sirt.isSelected())
            {
                bizimUser.sirtaOdak();
            }
            if (gogus.isSelected())
            {
                bizimUser.gogseOdak();
            }
            if (bacak.isSelected())
            {
                bizimUser.bacagaOdak();
            }

            programiGosterenPaneliOlustur();
            bizimUser.programiYazdir();
        }   
    }

    public void programiGosterenPaneliOlustur()
    {
        programiGosterenPanel = new JPanel();
        ArrayList<ArrayList<Exercises>> adaminProgrami = bizimUser.programiDondur();

        for (int i = 0; i < adaminProgrami.size(); i ++)
        {
            JPanel yalandanPanel = new JPanel(new GridLayout(adaminProgrami.get(i).size() + 1, 1));
            yalandanPanel.add(new JLabel((i + 1) + ". GUN"));

            for (int j = 0; j < adaminProgrami.get(i).size(); j++)
            {
                yalandanPanel.add(new JLabel(adaminProgrami.get(i).get(j).getIsim()));
            }
            programiGosterenPanel.add(yalandanPanel);
        }

        this.add(programiGosterenPanel, BorderLayout.CENTER);
        repaint();
    }

    
}
