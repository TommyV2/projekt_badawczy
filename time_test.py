import time
import requests
import pandas as pd


apps = {
    'laravel_php': '8000',
    'kotlin_ktor': '8001',
    'julia_genie': '8002',
    # 'js_express': '8003',
    # 'python_fastapi': '8004',
    # 'java_spring': '8006',
    # 'rust_rocket': '8007',
    # 'aspnetapp': '8008',
    # 'go_gin': '8009',
    # 'ruby_on_rails': '8010'
}

endpoints = [('response', 'GET'), ('database_read', 'GET'), ('database_write', 'POST'), ('product/5', 'POST')]

def run_test_get(url, retry_number):
    avg_time = 0
    results = []
    for i in range(retry_number):
        start = time.time()
        response = requests.get(url)
        end = time.time()
        avg_time += (end-start)
        results.append(end-start)
    return (avg_time/retry_number, results)

def run_test_post(url, retry_number):
    avg_time = 0
    results = []
    body_data = {'product_name': 'Generated', 'product_price': 999}
    for i in range(retry_number):
        start = time.time()
        response = requests.post(url, data=body_data)
        end = time.time()
        avg_time += (end-start)
        results.append(end-start)
    return (avg_time/retry_number, results)

def append_to_file(text):
    with open("results.txt", "a") as file1:
        file1.write(f"{text}\n")

retry_number = 100

for key in apps:
    port = apps[key]
    for endpoint in endpoints:
        action_name = endpoint[0]
        action_type = endpoint[1]
        url = f'http://localhost:{port}/{action_name}'
        if action_type == 'GET':
            average_time, results = run_test_get(url, retry_number)
        else:
            average_time, results = run_test_post(url, retry_number)
        
        append_to_file(f'{key}: {action_name} time = {average_time}')
        df = pd.DataFrame(results, columns = ['time'])
        stats = df[["time"]].describe()
        append_to_file(stats)
    append_to_file('===========================')
    

