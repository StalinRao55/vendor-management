const { test, expect } = require('playwright/test');

const BASE_URL = 'http://localhost:5500';

const pages = [
  '/index.html',
  '/login.html',
  '/vendor-registration.html',
  '/admin-dashboard.html',
  '/admin-vendor-approval.html',
  '/vendor-dashboard.html',
  '/employee-dashboard.html'
];

test('smoke: every page loads without crash', async ({ page }) => {
  const jsErrors = [];
  page.on('pageerror', (err) => jsErrors.push(err.message));

  for (const route of pages) {
    const response = await page.goto(`${BASE_URL}${route}`, { waitUntil: 'domcontentloaded' });
    expect(response, `${route} should return a response`).not.toBeNull();
    expect(response.status(), `${route} should be HTTP 200`).toBe(200);
    await page.waitForTimeout(350);
  }

  expect(jsErrors, `Found JS runtime errors:\n${jsErrors.join('\n')}`).toEqual([]);
});

test('role cards route to expected login URLs', async ({ page }) => {
  await page.goto(`${BASE_URL}/index.html`, { waitUntil: 'domcontentloaded' });

  const roleLinks = await page.locator('.roles-grid .role-card').evaluateAll((nodes) =>
    nodes.map((n) => n.getAttribute('href'))
  );

  expect(roleLinks).toEqual([
    'login.html?role=admin',
    'login.html?role=vendor',
    'login.html?role=employee'
  ]);
});

test('vendor registration stepper next/back works', async ({ page }) => {
  await page.goto(`${BASE_URL}/vendor-registration.html`, { waitUntil: 'domcontentloaded' });

  await page.fill('input[name="name"]', 'Smoke Vendor');
  await page.fill('input[name="contactPerson"]', 'Test User');
  await page.fill('input[name="email"]', 'smoke@example.com');
  await page.fill('input[name="phone"]', '9999999999');
  await page.fill('input[name="serviceType"]', 'IT Services');
  await page.selectOption('select[name="category"]', 'IT_SERVICES');
  await page.click('button:has-text("Continue")');
  await expect(page.locator('#step2.form-step.active')).toBeVisible();

  await page.fill('input[name="companyName"]', 'Smoke Co');
  await page.fill('input[name="gstNumber"]', '22AAAAA0000A1Z5');
  await page.fill('input[name="panNumber"]', 'AAAAA0000A');
  await page.click('#step2 button:has-text("Back")');
  await expect(page.locator('#step1.form-step.active')).toBeVisible();
});
