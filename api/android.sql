-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 20 Mar 2025 pada 10.50
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
(24, 'gani ', '', '', '', '', '', 'gani@gmail.com', '25d55ad283aa400af464c76d713c07ad'),
(25, 'eko', 'bima', 'Semarang', 'Jateng', '86548', '865467', 'eko@gmail.com', 'e10adc3949ba59abbe56e057f20f883e');

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

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `mahasiswa`
--
ALTER TABLE `mahasiswa`
  ADD PRIMARY KEY (`nim`);

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
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `tbl_pelanggan`
--
ALTER TABLE `tbl_pelanggan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT untuk tabel `tbl_produk`
--
ALTER TABLE `tbl_produk`
  MODIFY `id_produk` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
