-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 10, 2022 at 10:55 PM
-- Server version: 10.4.18-MariaDB
-- PHP Version: 8.0.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `uas`
--

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

CREATE TABLE `account` (
  `username` varchar(15) NOT NULL,
  `email` varchar(30) NOT NULL,
  `peran` varchar(10) NOT NULL,
  `password` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `account`
--

INSERT INTO `account` (`username`, `email`, `peran`, `password`) VALUES
('admin1', 'admin1@mail.com', 'admin', 'admin1'),
('admin2', 'admin2@gmail.com', 'admin', 'admin2'),
('AntoDo2', 'Anton@Do.com', 'user', 'AntoDo'),
('DenZera1', 'Denize@Cezera.com', 'user', 'Denzera'),
('Marin3', 'Marin@Luca.com', 'user', 'Marin'),
('Simon5', 'Simon@S.com', 'user', 'Simon5'),
('ViviN8', 'Vivi@Neil.com', 'user', 'ViviNeil');

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id` int(11) NOT NULL,
  `nama` varchar(20) NOT NULL,
  `email` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id`, `nama`, `email`) VALUES
(1, 'admin1', 'admin1g@mail.com'),
(2, 'admin2', 'admin2@gmail.com'),
(8, 'admin8', 'admin8@gmail.com');

-- --------------------------------------------------------

--
-- Table structure for table `intern`
--

CREATE TABLE `intern` (
  `id` int(11) NOT NULL,
  `nama` varchar(20) NOT NULL,
  `umur` varchar(2) NOT NULL,
  `jenkel` varchar(1) NOT NULL,
  `email` varchar(30) NOT NULL,
  `alamat` varchar(100) NOT NULL,
  `telpon` varchar(15) NOT NULL,
  `gambar` varchar(50) NOT NULL,
  `performa` varchar(15) NOT NULL,
  `project` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `intern`
--

INSERT INTO `intern` (`id`, `nama`, `umur`, `jenkel`, `email`, `alamat`, `telpon`, `gambar`, `performa`, `project`) VALUES
(1, 'Denize Cezera', '20', 'L', 'Denize@Cezera.com', 'Jl. Bunga Bahagia ', '080120220401', 'https://i.ibb.co/t3snVGM/user.png', 'Cukup', 'Dental Plus'),
(2, 'Marin Luca', '21', 'P', 'Marin@Luca.com', 'AEOn', '081471261234', 'https://i.ibb.co/t3snVGM/user.png', 'Baik', '-'),
(3, 'Simon Sebastian', '26', 'L', 'Simon@S.com', 'visby', '081481371332', 'https://i.ibb.co/t3snVGM/user.png', 'Buruk', '-'),
(4, 'Anton Dominika', '27', 'L', 'Anton@Do.com', 'finland', '08462551535', 'https://i.ibb.co/t3snVGM/user.png', 'Cukup', 'Opera V18'),
(5, 'Vivi Neilos', '26', 'P', 'Vivi@Neil.com', 'JL.alamat123', '12465381752', 'https://i.ibb.co/t3snVGM/user.png', 'Baik', 'vivo'),
(6, 'Daniil Benatrix', '28', 'L', 'Daniil@Bx.com', 'JL. Luar Angkasa no.100', '087612345678', 'https://i.ibb.co/t3snVGM/user.png', 'Sangat Baik', 'mozilla');

-- --------------------------------------------------------

--
-- Table structure for table `project`
--

CREATE TABLE `project` (
  `id` int(11) NOT NULL,
  `namaproject` varchar(50) NOT NULL,
  `pic` varchar(50) NOT NULL,
  `startdate` varchar(15) NOT NULL,
  `enddate` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `project`
--

INSERT INTO `project` (`id`, `namaproject`, `pic`, `startdate`, `enddate`) VALUES
(1, 'Dental Plus', 'Denize Cezera', '2022-01-01', '29/5/2031'),
(8, 'vivo', 'Vivi Neilos', '20/7/2021', '-'),
(13, 'Opera V19', 'Anton Dominika', '15/4/2025', '-'),
(14, 'mozilla', 'Daniil Benatrix', '16/8/2027', '-');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`username`);

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `intern`
--
ALTER TABLE `intern`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `project`
--
ALTER TABLE `project`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `intern`
--
ALTER TABLE `intern`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `project`
--
ALTER TABLE `project`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
