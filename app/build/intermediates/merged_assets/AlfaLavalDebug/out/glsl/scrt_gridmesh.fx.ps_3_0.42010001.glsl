precision mediump float;
uniform sampler2D IndexingTexture;
varying highp vec4 xlv_TEXCOORD0;
void main ()
{
  highp vec2 x_1;
  x_1 = (vec2(0.5, 0.5) - abs((xlv_TEXCOORD0.xy - vec2(0.5, 0.5))));
  bvec2 tmpvar_2;
  tmpvar_2 = lessThan (x_1, vec2(0.0, 0.0));
  if (any(tmpvar_2)) {
    discard;
  };
  highp vec4 tmpvar_3;
  lowp vec4 tmpvar_4;
  tmpvar_4 = texture2D (IndexingTexture, xlv_TEXCOORD0.zw);
  tmpvar_3 = tmpvar_4.zyxw;
  gl_FragData[0] = tmpvar_3;
}

