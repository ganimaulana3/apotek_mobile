-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 26 Bulan Mei 2025 pada 13.52
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `android`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `mahasiswa`
--

CREATE TABLE `mahasiswa` (
  `nim` varchar(30) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `telp` varchar(30) NOT NULL,
  `email` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `mahasiswa`
--

INSERT INTO `mahasiswa` (`nim`, `nama`, `telp`, `email`) VALUES
('', '', '', ''),
('A22.2010.00001', 'Damar Sulistiyono', '0856672723', 'DamarSulis@gmail.com'),
('A22.2010.00002', 'Satria Nugraha', '0853287334', 'Satrio@yahoo.com'),
('A22.2010.00003', 'Nia Prameswari', '0856788349', 'nia@gmail.com'),
('A22.2010.00004', 'Wikan Cahyadi', '0828877493', 'W2010@gmail.com'),
('A22.2010.00005', 'Darul Alhadi', '', ''),
('A22.2010.00006', 'Amin Pambudi', '024897743', 'AmPa@yahoo.com'),
('A22.2010.00007', 'Sukmajaya Narakarti', '0856887722', 'Sukma2010@gmail.com'),
('A22.2010.00008', 'Linda Oktaviani', '0812239288', 'lilinokta@gmail.com'),
('A22.2010.00009', 'Kinasih Tunggadewi', '0822667744', 'nasih@yahoo.com'),
('A22.2010.00010', 'Jamal Hendrayana', '0812233778', 'jamalhendra@gmail.co'),
('A22.2023.03031', 'GANI MAULANA', '082317330549', 'gani@gmail.com');

-- --------------------------------------------------------

--
-- Struktur dari tabel `produk`
--

CREATE TABLE `produk` (
  `id_produk` int(11) NOT NULL,
  `nm_produk` varchar(100) NOT NULL,
  `kategori` varchar(100) NOT NULL,
  `deskripsi` text NOT NULL,
  `harga` int(11) NOT NULL,
  `stok` int(11) NOT NULL,
  `img_produk` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `produk`
--

INSERT INTO `produk` (`id_produk`, `nm_produk`, `kategori`, `deskripsi`, `harga`, `stok`, `img_produk`) VALUES
(1, 'Paracetamol 500mg', 'Obat Bebas', 'Obat pereda demam dan nyeri', 5000, 1, 'Paracetamol_500mg.jpeg'),
(2, 'Amoxicillin 500mg', 'Obat Resep', 'Antibiotik untuk infeksi bakteri', 12000, 0, 'Amoxicillin_500mg.jpeg'),
(3, 'Panadol Extra', 'Obat Bebas', 'Mengandung parasetamol dan kafein untuk sakit kepala', 7500, 0, 'Panadol_Extra.jpeg'),
(4, 'Bodrex', 'Obat Bebas', 'Obat sakit kepala dan flu ringan', 4000, 1, 'Bodrex.jpeg'),
(5, 'Promag', 'Suplemen', 'Obat maag dalam bentuk tablet kunyah', 6000, 0, 'Promag.jpeg'),
(6, 'Diapet', 'Suplemen', 'Obat herbal untuk diare', 6500, 1, 'Diapet.jpeg'),
(7, 'Sanmol Syrup', 'Obat Bebas', 'Obat demam untuk anak dalam bentuk sirup', 8000, 2, 'Sanmol_Syrup.jpeg'),
(8, 'Captopril 25mg ', 'Obat Resep', 'Obat untuk menurunkan tekanan darah tinggi (hipertensi) dan mengatasi gagal jantung', 6000, 0, 'Captopril_25mg.jpeg'),
(9, 'OBH Combi', 'Obat Bebas', 'Obat batuk berdahak herbal', 10000, 1, 'OBH_Combi.jpeg'),
(10, 'Lacto-B', 'Suplemen', 'Probiotik untuk kesehatan pencernaan', 25000, 1, 'Lacto-B.jpeg'),
(11, 'Actifed', 'Obat Resep', 'Obat flu dan alergi, kombinasi antihistamin', 18000, 1, 'Actifed.jpeg'),
(12, 'Inzana', 'Suplemen', 'Suplemen penambah daya tahan tubuh', 15000, 1, 'Inzana.jpeg'),
(13, 'Antangin JRG', 'Suplemen', 'Obat herbal untuk masuk angin', 3000, 1, 'Antangin_JRG.jpeg'),
(14, 'Neuremacyl', 'Obat Resep', 'Obat nyeri saraf dan otot', 22000, 1, 'Neuremacyl.jpeg'),
(15, 'Vitamin C 1000mg', 'Suplemen', 'Tablet vitamin C untuk daya tahan tubuh', 12000, 1, 'Vitamin_C_1000mg.jpeg'),
(16, 'Cetirizine', 'Obat Resep', 'Antihistamin untuk alergi dan flu', 6500, 1, 'Cetirizine.jpeg'),
(17, 'Ibuprofen', 'Obat bebas', 'Obat antiinflamasi non-steroid (OAINS) yang digunakan untuk nyeri, demam, dan peradangan ringan', 8000, 1, 'Ibuprofen.jpeg'),
(18, 'CTM', 'Obat bebas', 'Antihistamin yang digunakan untuk alergi ringan seperti bersin, gatal, atau hidung meler', 7500, 1, 'CTM_Chlorpheniramine_Maleate.jpeg'),
(19, 'Guaifenesin', 'Obat bebas', 'Obat ekspektoran untuk membantu mengencerkan dahak dan meredakan batuk berdahak ', 9000, 1, 'Guaifenesin.jpeg'),
(20, 'Anacetin', 'Obat Resep', 'Obat anti nyeri dan inflamasi', 14000, 1, 'Anacetin.jpeg');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tbl_pelanggan`
--

CREATE TABLE `tbl_pelanggan` (
  `id` int(11) NOT NULL,
  `nama` varchar(200) NOT NULL,
  `alamat` varchar(200) NOT NULL,
  `kota` char(100) NOT NULL,
  `provinsi` char(100) NOT NULL,
  `kodepos` varchar(20) NOT NULL,
  `telp` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tbl_pelanggan`
--

INSERT INTO `tbl_pelanggan` (`id`, `nama`, `alamat`, `kota`, `provinsi`, `kodepos`, `telp`, `email`, `password`) VALUES
(19, 'zaki', '', '', '', '', '', 'zaki@gmail.com', '25d55ad283aa400af464c76d713c07ad'),
(20, 'novan', '', '', '', '', '', 'novan@gmail.com', '25d55ad283aa400af464c76d713c07ad'),
(21, 'farhan', '', '', '', '', '', 'farhan@gmail.com', 'e10adc3949ba59abbe56e057f20f883e'),
(22, 'zidan', '', '', '', '', '', 'zidan@gmail.com', 'e10adc3949ba59abbe56e057f20f883e'),
(23, 'sarep ', '', '', '', '', '', 'sarep@gmail.com', '202cb962ac59075b964b07152d234b70'),
(24, 'gani', 'randegan 1', 'banjar', 'jawa barat', '46333', '082317330549', 'gani@gmail.com', '25d55ad283aa400af464c76d713c07ad'),
(25, 'eko', 'bima', 'Semarang', 'Jateng', '86548', '865467', 'eko@gmail.com', 'e10adc3949ba59abbe56e057f20f883e'),
(26, 'zikri', '', '', '', '', '', 'zikri@gmail.com', '202cb962ac59075b964b07152d234b70'),
(28, 'yuda', '', '', '', '', '', 'yuda@gmail.com', 'e10adc3949ba59abbe56e057f20f883e'),
(29, 'raka', '', '', '', '', '', 'raka@gmail.com', '202cb962ac59075b964b07152d234b70'),
(30, 'andi', 'smg', 'jalan pemuda', 'Jateng', '28636', '0553523', 'andi@gmail.com', '827ccb0eea8a706c4c34a16891f84e7b');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tbl_produk`
--

CREATE TABLE `tbl_produk` (
  `id_produk` int(20) NOT NULL,
  `nm_produk` varchar(100) NOT NULL,
  `harga` int(50) NOT NULL,
  `stok` int(50) NOT NULL,
  `img_produk` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tbl_produk`
--

INSERT INTO `tbl_produk` (`id_produk`, `nm_produk`, `harga`, `stok`, `img_produk`) VALUES
(1, 'ASUS ROG Strix G15', 22999000, 5, 'https://images.tokopedia.net/img/cache/700/VqbcmM/2021/9/4/5c7d00af-a1fa-4d2e-965b-f7ea4a90bdb1.jpg'),
(2, 'MacBook Air M2', 18999000, 8, 'https://asset.kompas.com/crops/SUPKwK6D9QPdfzLwfvuUkHoM1ZI=/0x0:1386x924/1200x800/data/photo/2022/09/08/6319aeffb0590.jpg'),
(3, 'Samsung Galaxy S23 Ultra', 20999000, 10, 'https://www.static-src.com/wcsstore/Indraprastha/images/catalog/full//112/MTA-92941789/br-m036969-02885_samsung-galaxy-s23-ultra-8-256gb_full01.jpg'),
(4, 'iPhone 14 Pro Max', 23999000, 7, 'https://images-cdn.ubuy.co.id/6539d3133e361e207169ab94-restored-apple-iphone-14-pro-max.jpg'),
(5, 'ASUS Zenbook A14', 12000000, 11, 'https://dlcdnwebimgs.asus.com/gain/14c6be57-e7d1-462c-a109-4f93c353d93c/');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tbl_user`
--

CREATE TABLE `tbl_user` (
  `username` char(20) NOT NULL,
  `password` varchar(100) NOT NULL,
  `status` int(11) NOT NULL,
  `email` varchar(80) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tbl_user`
--

INSERT INTO `tbl_user` (`username`, `password`, `status`, `email`) VALUES
('admin', '123', 0, 'admin@gmail.com'),
('gani', '123', 1, 'gani@gmail.com');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tbl_viewer`
--

CREATE TABLE `tbl_viewer` (
  `id_viewer` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `id_produk` int(100) NOT NULL,
  `nm_viewer` varchar(50) NOT NULL,
  `total_viewer` int(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tbl_viewer`
--

INSERT INTO `tbl_viewer` (`id_viewer`, `id_user`, `id_produk`, `nm_viewer`, `total_viewer`) VALUES
(9, 24, 1, 'gani', 3),
(10, 24, 2, 'gani', 2),
(11, 24, 3, 'gani', 1),
(12, 24, 4, 'gani', 1);

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `mahasiswa`
--
ALTER TABLE `mahasiswa`
  ADD PRIMARY KEY (`nim`);

--
-- Indeks untuk tabel `produk`
--
ALTER TABLE `produk`
  ADD PRIMARY KEY (`id_produk`);

--
-- Indeks untuk tabel `tbl_pelanggan`
--
ALTER TABLE `tbl_pelanggan`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `tbl_produk`
--
ALTER TABLE `tbl_produk`
  ADD PRIMARY KEY (`id_produk`);

--
-- Indeks untuk tabel `tbl_user`
--
ALTER TABLE `tbl_user`
  ADD PRIMARY KEY (`username`);

--
-- Indeks untuk tabel `tbl_viewer`
--
ALTER TABLE `tbl_viewer`
  ADD PRIMARY KEY (`id_viewer`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `produk`
--
ALTER TABLE `produk`
  MODIFY `id_produk` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT untuk tabel `tbl_pelanggan`
--
ALTER TABLE `tbl_pelanggan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT untuk tabel `tbl_produk`
--
ALTER TABLE `tbl_produk`
  MODIFY `id_produk` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT untuk tabel `tbl_viewer`
--
ALTER TABLE `tbl_viewer`
  MODIFY `id_viewer` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
