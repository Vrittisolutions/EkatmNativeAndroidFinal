uniform highp mat4 matProj;
uniform highp mat4 matWorld;
uniform highp mat4 matWorldView;
uniform highp vec4 PackedFloatParams;
uniform highp vec4 WaterFallSliceOffsset;
uniform highp vec4 TextureDimensionsInv;
uniform highp vec4 WorldXZOffsetSize;
uniform sampler2D HeightMapTexture;
attribute highp vec4 vPosition;
attribute highp vec3 vNormal;
varying highp vec4 xlv_TEXCOORD1;
varying highp vec3 xlv_TEXCOORD2;
void main ()
{
  highp vec4 tmpvar_1;
  tmpvar_1.x = vPosition.x;
  tmpvar_1.z = (vPosition.z + (WaterFallSliceOffsset.y * (1.0/(matWorld[2].z))));
  highp vec2 tmpvar_2;
  tmpvar_2.x = matWorld[0].w;
  tmpvar_2.y = WaterFallSliceOffsset.y;
  highp vec2 _texCoord_3;
  _texCoord_3 = ((tmpvar_2 - WorldXZOffsetSize.xy) * (1.0/(WorldXZOffsetSize.zw)));
  _texCoord_3.x = mix (TextureDimensionsInv.x, TextureDimensionsInv.z, _texCoord_3.x);
  _texCoord_3.y = mix (TextureDimensionsInv.y, TextureDimensionsInv.w, _texCoord_3.y);
  lowp vec4 tmpvar_4;
  tmpvar_4 = texture2DLod (HeightMapTexture, _texCoord_3, 0.0);
  highp vec4 rgba_5;
  rgba_5 = tmpvar_4.zyxw;
  tmpvar_1.y = (vPosition.y + (clamp (
    (matWorld[1].w * ((dot (rgba_5.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8)) * PackedFloatParams.y) + PackedFloatParams.x))
  , WaterFallSliceOffsset.z, WaterFallSliceOffsset.w) * (1.0/(matWorld[1].y))));
  tmpvar_1.w = 1.0;
  highp mat3 tmpvar_6;
  tmpvar_6[0] = matWorld[0].xyz;
  tmpvar_6[1] = matWorld[1].xyz;
  tmpvar_6[2] = matWorld[2].xyz;
  gl_Position = (tmpvar_1 * (matWorldView * matProj));
  xlv_TEXCOORD1 = (tmpvar_1 * matWorld);
  xlv_TEXCOORD2 = (vNormal * tmpvar_6);
}

