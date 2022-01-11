<?php
	error_reporting(0);
	define('DB_USER', "root");
	define('DB_PASSWORD', "");
	define('DB_DATABASE', "uas");
	define('DB_SERVER', "localhost");

	class DB_CONNECT {
		public $con;
		
		function __construct() {
			$this->connect();
		}

		function __destruct() {
			$this->close();
		}

		function connect() {
			$this->$con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

			if (mysqli_connect_errno()) {
				exit();
			}

			return $this->$con;
		}
		
		function close() {
			mysqli_close($this->$con);
		}
	}


