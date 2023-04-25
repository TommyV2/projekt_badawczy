<?php

use App\Models\Product;
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
    return Product::all();
});

Route::post('/database_write', function (Request $request) {
    $product = new Product;
    $product->product_name = $request->name;
    $product->product_price = $request->price;
    $product->save();
});

Route::post('/database_write_many', function (Request $request) {
    for ($i = 1; $i <= 10; $i++) {
        $product = new Product;
        $product->product_name = "Some product";
        $product->product_price = $i;
        $product->save();
    }
});

Route::get('/product/{id}', function (int $id) {
    return Product::findOrFail($id);
});

Route::get('/database_read_conn', function () {
    $products = DB::connection('pgsql')->select("select * from product");
    return $products;
});

Route::post('/database_write_conn', function (Request $request) {
    $query = "insert into product (product_name, product_price) values ('".$request->input("name")."','".$request->input("price")."')";
    DB::connection('pgsql')->insert($query);
});