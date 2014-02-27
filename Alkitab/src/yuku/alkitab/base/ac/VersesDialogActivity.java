package yuku.alkitab.base.ac;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import yuku.alkitab.base.ac.base.BaseActivity;
import yuku.alkitab.base.dialog.VersesDialog;
import yuku.alkitab.base.util.TargetDecoder;
import yuku.alkitab.util.IntArrayList;
import yuku.alkitabintegration.display.Launcher;

/**
 * Transparent activity that shows verses dialog only.
 */
public class VersesDialogActivity extends BaseActivity {
	public static final String TAG = VersesDialogActivity.class.getSimpleName();

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// now only supports target (decoded using TargetDecoder)
		final String target = getIntent().getStringExtra("target");
		if (target == null) {
			finish();
			return;
		}

		final IntArrayList ariRanges = TargetDecoder.decode(target);
		if (ariRanges == null) {
			new AlertDialog.Builder(this)
			.setMessage("Could not understand target: " + target)
			.setPositiveButton("OK", null)
			.show()
			.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(final DialogInterface dialog) {
					finish();
				}
			});
			return;
		}

		final VersesDialog versesDialog = VersesDialog.newInstance(ariRanges);
		versesDialog.setListener(new VersesDialog.VersesDialogListener() {
			@Override
			public void onVerseSelected(final VersesDialog dialog, final int ari) {
				startActivity(Launcher.openAppAtBibleLocation(ari));
				finish();
			}
		});
		versesDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(final DialogInterface dialog) {
				finish();
			}
		});

		versesDialog.show(getSupportFragmentManager(), VersesDialog.class.getSimpleName());
	}
}
