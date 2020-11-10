package util;

import android.content.Context;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import codecanyon.jagatpharma.R;

/**
 * Created by Rajesh on 2017-09-13.
 */

public class ExpandableTextview {

    static Context context;

    public static void makeTextViewResizable(Context context1, final TextView tv, final int maxLine, final String expandText,
                                             final boolean viewMore, final boolean showUnderline) {

        context = context1;

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore, showUnderline), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore, showUnderline), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore, showUnderline), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText,
                                                                            final boolean viewMore, final boolean showUnderline) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            if (showUnderline) {
                ssb.setSpan(new MySpannable(false) {
                    @Override
                    public void onClick(View widget) {
                        if (viewMore) {
                            viewMore(tv, showUnderline);
                        } else {
                            viewLess(tv, showUnderline);
                        }
                    }
                }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);
            } else {
                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        if (viewMore) {
                            viewMore(tv, showUnderline);
                        } else {
                            viewLess(tv, showUnderline);
                        }

                    }
                }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);
            }
        }
        return ssb;
    }

    private static void viewMore(TextView tv, boolean showUnderline) {
        tv.setLayoutParams(tv.getLayoutParams());
        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
        tv.invalidate();
        makeTextViewResizable(context, tv, -1, context.getResources().getString(R.string.view_less), false, showUnderline);
    }

    private static void viewLess(TextView tv, boolean showUnderline) {
        tv.setLayoutParams(tv.getLayoutParams());
        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
        tv.invalidate();
        makeTextViewResizable(context, tv, 3, context.getResources().getString(R.string.view_more), true, showUnderline);
    }

    public static class MySpannable extends ClickableSpan {

        private boolean isUnderline = false;

        /**
         * Constructor
         */
        public MySpannable(boolean isUnderline) {
            this.isUnderline = isUnderline;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(isUnderline);
            ds.setColor(context.getResources().getColor(R.color.black));
        }

        @Override
        public void onClick(View widget) {

        }
    }

}
