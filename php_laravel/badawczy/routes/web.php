<?php

use Illuminate\Support\Facades\Route;
use Illuminate\Support\Facades\DB;
use Illuminate\Http\Request;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/response', function () {
    return "Hejka, tu projekt badawczy";
});

Route::get('/database_read', function () {
    $products = DB::connection('pgsql')->select("select * from product");
    return $products;
});

Route::post('/database_write', function (Request $request) {
    $query = "insert into product (product_name, product_price) values ('".$request->input("product_name")."','".$request->input("product_price")."')";
    DB::connection('pgsql')->insert($query);
});

