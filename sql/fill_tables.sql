do $$
begin
for r in 1..10 loop
insert into product(product_name, product_price) values('Product' || r, 20 + floor(random() * 500)::int);
end loop;
end;
$$;

do $$
begin
for r in 1..10 loop
insert into products(product_name, product_price) values('Product' || r, 20 + floor(random() * 500)::int);
end loop;
end;
$$;