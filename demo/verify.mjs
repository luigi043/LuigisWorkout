import assert from 'node:assert/strict';
import { mkdir } from 'node:fs/promises';
import { pathToFileURL } from 'node:url';

const { chromium } = await import(process.env.PLAYWRIGHT_MODULE
  ? pathToFileURL(process.env.PLAYWRIGHT_MODULE).href : 'playwright');
const base = process.env.DEMO_URL || 'https://luigis-workout-portfolio.vercel.app/';
const folder = new URL('../docs/verification/browser/', import.meta.url);
await mkdir(folder, { recursive: true });
const browser = await chromium.launch({ executablePath: process.env.BROWSER_EXECUTABLE });
try {
  for (const width of [320, 390, 1440]) {
    const page = await browser.newPage({ viewport: { width, height: 844 }, hasTouch: width < 600 });
    const errors = [];
    page.on('pageerror', error => errors.push(error.message));
    const tab = name => page.getByRole('navigation').getByRole('button', { name, exact: true }).click();
    const capture = async name => {
      assert.ok(await page.locator('.phone').evaluate(node => node.clientWidth) <= 430);
      assert.ok(await page.evaluate(() => document.documentElement.scrollWidth <= innerWidth));
      if (width !== 320) await page.screenshot({ path: new URL(`${width}-${name}.png`, folder).pathname.replace(/^\/(?=[A-Z]:)/i, '').replaceAll('%20', ' '), fullPage: true });
    };
    assert.equal((await page.goto(base)).status(), 200);
    await page.getByRole('button', { name: 'Add a glass' }).click();
    await page.getByText('1 of 8 glasses').waitFor();
    await capture('01-home');
    await tab('Meals');
    await page.getByRole('button', { name: 'Try a sample' }).click();
    await page.getByRole('button', { name: 'Save meal' }).click();
    await page.getByText('Breakfast · Greek yogurt and oats · 320 kcal').waitFor();
    await capture('02-meals');
    await tab('Workout');
    await page.getByLabel('Training day').selectOption('Legs');
    await page.getByRole('button', { name: 'Start', exact: true }).click();
    await page.getByRole('button', { name: 'Pause', exact: true }).waitFor();
    await page.getByRole('checkbox', { name: 'Goblet squat' }).check();
    await capture('03-workout');
    await page.getByRole('button', { name: 'Save workout' }).click();
    await page.getByText(/^Legs · 1 exercises/).waitFor();
    await page.getByLabel('Weight (kg)').fill('82.5');
    await page.getByRole('button', { name: 'Save weight' }).click();
    await page.getByText('82.5 kg', { exact: true }).waitFor();
    await capture('04-progress');
    await page.reload();
    await page.getByText('320 kcal across 1 logged meals').waitFor();
    await tab('Progress');
    await page.getByText('82.5 kg', { exact: true }).waitFor();
    await page.getByText(/^Legs · 1 exercises/).waitFor();
    page.once('dialog', dialog => dialog.accept());
    await page.getByRole('button', { name: 'Reset demo' }).click();
    await page.getByText('0 kcal across 0 logged meals').waitFor();
    assert.deepEqual(errors, []);
    await page.close();
    console.log(`${width}px: layout, logging, workout, persistence and reset passed`);
  }
} finally {
  await browser.close();
}
